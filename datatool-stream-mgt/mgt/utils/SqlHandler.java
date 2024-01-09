/*
 * 文 件 名:  SqlHandler.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  s30009470
 * 修改时间： 2021/2/10
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.stream.mgt.utils;

import com.huawei.hicampus.campuscommon.common.util.ClearSensitiveDataUtil;
import com.huawei.smartcampus.datatool.entity.DtCipherVariableEntity;
import com.huawei.smartcampus.datatool.entity.DtEnvironmentVariableEntity;
import com.huawei.smartcampus.datatool.entity.SystemPropertyEntity;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;
import com.huawei.smartcampus.datatool.repository.DtCipherVariableRepository;
import com.huawei.smartcampus.datatool.repository.DtEnvironmentVariableRepository;
import com.huawei.smartcampus.datatool.repository.SystemPropertiesRepository;
import com.huawei.smartcampus.datatool.utils.DataToolCryptor;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Flink SQL处理类
 *
 * @author s30009470
 * @version [SmartCampus V100R001C00, 2021/2/10]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@Component
public class SqlHandler {
    /**
     * 匹配 {{xxxxx}}，但是不匹配{{Env.get\(xxxxx\)}}和{{Cipher.get\(xxxx\)}}
     * 同时避免redos攻击
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([a-zA-Z0-9-_.@]{1,500})}}");

    /**
     * 匹配新的环境变量使用方式{{Env.get\(xxxxx\)}}
     * 同时避免redos攻击
     */
    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\{\\{Env.get\\(([a-zA-Z0-9-_]{1,64})\\)}}");

    /**
     * 匹配新的密码箱使用方式{{Cipher.get\(xxxx\)}}
     * 同时避免redos攻击
     */
    private static final Pattern CIPHER_VAR_PATTERN = Pattern
            .compile("\\{\\{Cipher.get\\(([a-zA-Z0-9-_.@]{1,500})\\)}}");

    /**
     * 匹配$[cov_period]
     * 同时避免redos攻击
     */
    private static final Pattern SYSTEM_PROPERTY_PATTERN = Pattern
            .compile("\\{\\{Sys.get\\(([a-zA-Z0-9-_]{1,64})\\)}}");

    private static final String SQL_CONTENT_SPILT = "\\r?\\n";

    @Autowired
    private DtEnvironmentVariableRepository dtEnvironmentVariableRepository;

    @Autowired
    private DtCipherVariableRepository dtCipherVariableRepository;

    @Autowired
    private SystemPropertiesRepository systemPropertiesRepository;

    /**
     * sql替换变量
     *
     * @param sql sql
     * @param encoder encoder
     * @param decoder decoder
     * @return sql
     */
    public String sqlAfterReplaceVar(String sql, Base64.Encoder encoder, Base64.Decoder decoder) {
        String decodedContent = new String(decoder.decode(sql), StandardCharsets.UTF_8);
        List<String> keys = new ArrayList<>();
        addKeys(keys, decodedContent, VARIABLE_PATTERN);
        // 获取密码箱变量
        Map<String, String> cipherVariableMap = getCipherMap(keys, decodedContent);
        // 获取环境变量
        Map<String, String> envVariableMap = getEnvMap(keys, decodedContent);
        // 获取系统变量
        Map<String, String> systemPropertiesMap = getRefSystemProperties(decodedContent);
        String[] decodeLines = decodedContent.split(SQL_CONTENT_SPILT);
        List<String> linesAfterReplace = new ArrayList<>(decodeLines.length);
        for (String line : decodeLines) {
            String lineNoAnnotation = line;
            if (lineNoAnnotation.contains("--")) {
                lineNoAnnotation = lineNoAnnotation.substring(0, lineNoAnnotation.indexOf("--"));
            }
            if (StringUtils.isEmpty(lineNoAnnotation)) {
                linesAfterReplace.add(line);
                continue;
            }
            lineNoAnnotation = doReplaceVar(lineNoAnnotation, envVariableMap, cipherVariableMap);
            lineNoAnnotation = doReplaceEnv(lineNoAnnotation, envVariableMap);
            lineNoAnnotation = doReplaceCipher(lineNoAnnotation, cipherVariableMap);
            lineNoAnnotation = doReplaceSystemProperties(lineNoAnnotation, systemPropertiesMap);
            linesAfterReplace.add(lineNoAnnotation);
        }
        try {
            return new String(
                    encoder.encode(
                            String.join(System.lineSeparator(), linesAfterReplace).getBytes(StandardCharsets.UTF_8)),
                    StandardCharsets.UTF_8);
        } finally {
            // linesAfterReplace里是含有敏感信息的
            for (String line : linesAfterReplace) {
                ClearSensitiveDataUtil.clearPlainSensitiveData(line);
            }
        }
    }

    private String doReplaceEnv(String lineContent, Map<String, String> variableMap) {
        List<String> keys = new ArrayList<>(3);
        addKeys(keys, lineContent, ENV_VAR_PATTERN);
        String lineAfterReplace = lineContent;
        for (String key : keys) {
            if (variableMap.containsKey(key)) {
                lineAfterReplace = lineAfterReplace.replaceAll(String.format("\\{\\{Env.get\\(%s\\)}}", key),
                        variableMap.get(key));
            } else {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_UNKNOWN_VAR_OR_KEY, key);
            }
        }
        return lineAfterReplace;
    }

    private String doReplaceCipher(String lineContent, Map<String, String> cipherVariableMap) {
        List<String> keys = new ArrayList<>(3);
        addKeys(keys, lineContent, CIPHER_VAR_PATTERN);
        String lineAfterReplace = lineContent;
        for (String key : keys) {
            if (cipherVariableMap.containsKey(key)) {
                String plainCipherText = null;
                String quoteReplacement = null;
                try {
                    plainCipherText = DataToolCryptor.decodingSecret(cipherVariableMap.get(key));
                    quoteReplacement = Matcher.quoteReplacement(plainCipherText);
                    lineAfterReplace = lineAfterReplace.replaceAll(String.format("\\{\\{Cipher.get\\(%s\\)}}", key),
                            quoteReplacement);
                } finally {
                    // 清理敏感信息
                    ClearSensitiveDataUtil.clearPlainSensitiveData(plainCipherText);
                    ClearSensitiveDataUtil.clearPlainSensitiveData(quoteReplacement);
                }
            }
        }
        return lineAfterReplace;
    }

    private String doReplaceVar(String lineContent, Map<String, String> variableMap,
            Map<String, String> cipherVariableMap) {
        List<String> keys = new ArrayList<>(3);
        addKeys(keys, lineContent, VARIABLE_PATTERN);
        String lineAfterReplace = lineContent;
        for (String key : keys) {
            if (cipherVariableMap.containsKey(key)) {
                String plainCipherText = null;
                String quoteReplacement = null;
                try {
                    plainCipherText = DataToolCryptor.decodingSecret(cipherVariableMap.get(key));
                    quoteReplacement = Matcher.quoteReplacement(plainCipherText);
                    lineAfterReplace = lineAfterReplace.replaceAll(String.format("\\{\\{%s}}", key), quoteReplacement);
                } finally {
                    // 清理敏感信息
                    ClearSensitiveDataUtil.clearPlainSensitiveData(plainCipherText);
                    ClearSensitiveDataUtil.clearPlainSensitiveData(quoteReplacement);
                }
            } else if (variableMap.containsKey(key)) {
                lineAfterReplace = lineAfterReplace.replaceAll(String.format("\\{\\{%s}}", key), variableMap.get(key));
            } else {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_UNKNOWN_VAR_OR_KEY, key);
            }
        }
        return lineAfterReplace;
    }

    private String doReplaceSystemProperties(String sql, Map<String, String> systemPropertiesMap) {
        List<String> keys = new ArrayList<>(3);
        addKeys(keys, sql, SYSTEM_PROPERTY_PATTERN);
        String lineAfterReplace = sql;
        for (String key : keys) {
            if (systemPropertiesMap.containsKey(key)) {
                lineAfterReplace = lineAfterReplace.replaceAll(String.format("\\{\\{Sys.get\\(%s\\)}}", key),
                        systemPropertiesMap.get(key));
            } else {
                throw new DataToolRuntimeException(ExceptionCode.DATATOOL_UNKNOWN_VAR_OR_KEY, key);
            }
        }
        return lineAfterReplace;
    }

    /**
     * 寻找匹配内容，把它加到列表中
     *
     * @param keys 变量列表
     * @param content 识别内容
     * @param pattern 匹配正则
     */
    private void addKeys(List<String> keys, String content, Pattern pattern) {
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
    }

    private Map<String, String> getEnvMap(List<String> keys, String decodeSql) {
        // 如果旧方式为空，将新方式获取加到envKeys中
        List<String> envKeys = new ArrayList<>(keys);
        addKeys(envKeys, decodeSql, ENV_VAR_PATTERN);
        return dtEnvironmentVariableRepository.findDtEnvironmentVariableEntitiesByKeyIn(envKeys).stream()
                .collect(Collectors.toMap(DtEnvironmentVariableEntity::getKey, DtEnvironmentVariableEntity::getValue));
    }

    private Map<String, String> getCipherMap(List<String> keys, String decodeSql) {
        // 如果旧方式为空，将新方式获取加到keys中
        List<String> cipherKeys = new ArrayList<>(keys);
        addKeys(cipherKeys, decodeSql, CIPHER_VAR_PATTERN);
        return dtCipherVariableRepository.findCipherVariableEntitiesByKeyIn(cipherKeys).stream()
                .collect(Collectors.toMap(DtCipherVariableEntity::getKey, DtCipherVariableEntity::getValue));
    }

    /**
     * 从sql中，获取系统变量$[xxx]的名称，并根据名称从数据库获取值，组成map
     *
     * @param sql 解密后的flinksql
     * @return 系统变量的map
     */
    private Map<String, String> getRefSystemProperties(String sql) {
        List<String> keys = new ArrayList<>();
        addKeys(keys, sql, SYSTEM_PROPERTY_PATTERN);
        return systemPropertiesRepository.findSystemPropertyEntityByNameIn(keys).stream()
                .collect(Collectors.toMap(SystemPropertyEntity::getName, SystemPropertyEntity::getCurrentValue));
    }
}
