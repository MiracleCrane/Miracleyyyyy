/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package com.huawei.dataservice.sql.udf.udagg;

import com.huawei.dataservice.sql.udf.udagg.acc.UnificationAcc;
import com.huawei.smartcampus.datatool.utils.StringUtils;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.util.TimeUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MAP<String,String>类型聚合
 *
 * @author zwx632190
 * @version [SmartCampus V100R001C00, 2021/3/30]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
@FunctionHint(output = @DataTypeHint("Map<String, String>"))
public class UnificationMap extends UnificationBase<Map<String, String>> {
    private static final long serialVersionUID = -315810010138660552L;

    // 1小时对应的毫秒数
    private static final int HOUR_MILL_SECOND = 1000 * 60 * 60;

    // OC上报的 NULLVALUE 值
    private static final String NULL_VALUE = "NULLVALUE";

    // 记录所有设备最新记录的表
    private final Map<String, Map<String, VersionedField>> latestRecords = new ConcurrentHashMap<>();

    /**
     * COV滑动窗口，MAP数据补齐函数
     *
     * @param acc        聚合类
     * @param deviceId   设备id
     * @param windowSize 窗口大小
     * @param rowtime    事件时间
     * @param input      被聚合的MAP原始数据
     */
    @FunctionHint(accumulator = @DataTypeHint(value = "RAW", bridgedTo = UnificationAcc.class), input = {
            @DataTypeHint("STRING"), @DataTypeHint("STRING"), @DataTypeHint("TIMESTAMP"),
            @DataTypeHint("MAP<STRING, STRING>")})
    public void accumulate(UnificationAcc<Map<String, String>> acc, String deviceId, String windowSize,
                           LocalDateTime rowtime, Map<String, String> input) {
        Map<String, String> filteredMap = getFilteredMap(input);
        updateRecords(deviceId, windowSize, rowtime, filteredMap);
        Map<String, VersionedField> latestRecord = latestRecords.get(deviceId);
        Map<String, String> current = new HashMap<>();

        latestRecord.forEach((String key, VersionedField value) -> current.put(key, value.fieldValue));
        acc.setObj(current);
    }

    /**
     * 滚动窗口补齐
     *
     * @param acc 继承对象
     * @param input 被补齐的MAP对象
     */
    @FunctionHint(input = @DataTypeHint("Map<String, String>"))
    @Override
    public void accumulate(UnificationAcc<Map<String, String>> acc, Map<String, String> input) {
        Map<String, String> filteredMap = getFilteredMap(input);
        // 如果acc中的值为null或者空，则直接赋值in给acc中对象
        if (acc.getObj() == null || acc.getObj().isEmpty()) {
            acc.setObj(filteredMap);
        } else {
            Map<String, String> map = new HashMap<>();
            // 对acc迭代
            for (Map.Entry<String, String> entry : acc.getObj().entrySet()) {
                // 将值添加进集合
                map.put(entry.getKey(), entry.getValue());
            }
            // 对in迭代
            filteredMap.forEach(map::put);
            acc.setObj(map);
        }
    }

    @Override
    public void open(FunctionContext context) throws Exception {
        super.open(context);
    }

    /**
     * HOP窗口用到merge函数，合并结果
     *
     * @param acc 聚合实体
     * @param its 各个子任务的具体化实体
     */
    public void merge(UnificationAcc<Map<String, String>> acc, Iterable<UnificationAcc<Map<String, String>>> its) {
        for (UnificationAcc<Map<String, String>> accTmp : its) {
            acc.setObj(accTmp.getObj());
        }
    }

    /**
     * 将原始Map<String,String>转换为Map<String,VersiondField>，给属性打上version，version就是属性携带的eventime毫秒数
     *
     * @param rowtime eventtime
     * @param input 实时点位数据
     * @return Map<String, VersionedField>
     */
    private Map<String, VersionedField> getVersionedMap(LocalDateTime rowtime, Map<String, String> input) {
        long version = rowtime
                .toEpochSecond(ZoneOffset.ofHours(TimeZone.getDefault().getRawOffset() / HOUR_MILL_SECOND));
        Map<String, VersionedField> latestRecord = new ConcurrentHashMap<>();

        input.forEach((String key, String value) -> {
            VersionedField newField = new VersionedField(value, version);
            latestRecord.put(key, newField);
        });
        return latestRecord;
    }

    /**
     * 将 map 中的消息进行过滤，返回新 map
     *
     * @param input 点位数据
     * @return 被过滤的map
     */
    private Map<String, String> getFilteredMap(Map<String, String> input) {
        Map<String, String> filteredMap = new HashMap<>();
        input.forEach((String key, String value) -> {
            if (checkNotNull(value)) {
                filteredMap.put(key, value);
            }
        });
        return filteredMap;
    }

    /**
     * 检查属性值符合不为空的要求
     *
     * @param value 属性值
     * @return 属性值是否符合要求
     */
    private boolean checkNotNull(String value) {
        return StringUtils.isNotEmpty(value) && !NULL_VALUE.equals(value);
    }

    /**
     * 更新设备最新属性记录列表：
     * 1、用最新的记录的属性更新
     * 2、将超过窗口时长的属性删除
     *
     * @param deviceId 设备唯一标识
     * @param windowSize 窗口描述符，如：10s
     * @param rowtime eventtime
     * @param input 实时点位数据
     */
    private void updateRecords(String deviceId, String windowSize, LocalDateTime rowtime, Map<String, String> input) {
        Map<String, VersionedField> newRecord = getVersionedMap(rowtime, input);
        // 从记录表中取出上一次的记录
        Map<String, VersionedField> oldRecord = latestRecords.get(deviceId);
        if (oldRecord == null) {
            oldRecord = newRecord;
        } else {
            refreshOldRecords(oldRecord, newRecord);
        }

        checkAndResetField(oldRecord, windowSize, rowtime);
        latestRecords.put(deviceId, oldRecord);
    }

    /**
     * 使用新数据更新老数据
     *
     * @param oldRecord 老的记录
     * @param newRecord 新的记录
     */
    private void refreshOldRecords(Map<String, VersionedField> oldRecord, Map<String, VersionedField> newRecord) {
        newRecord.forEach((String key, VersionedField value) -> {
            if (oldRecord.get(key) == null) {
                oldRecord.put(key, value);
            } else {
                if (oldRecord.get(key).fieldVersion <= value.fieldVersion) {
                    oldRecord.put(key, value);
                }
            }
        });
    }

    /**
     * 将超过窗口时间的field剔除。这个由数据驱动，没有数据驱动无法剔除。
     *
     * @param record 已被更新过的记录
     * @param windowSize 窗口描述，如10s
     * @param rowtime 设备的eventtime
     */
    private void checkAndResetField(Map<String, VersionedField> record, String windowSize, LocalDateTime rowtime) {
        long currentTime = rowtime
                .toEpochSecond(ZoneOffset.ofHours(TimeZone.getDefault().getRawOffset() / HOUR_MILL_SECOND));
        long windowSizeSeconds = TimeUtils.parseDuration(windowSize).toMillis() / 1000;
        record.entrySet().removeIf(entry -> currentTime - entry.getValue().getFieldVersion() >= windowSizeSeconds);
    }

    /**
     * 字段Version实体
     */
    public static class VersionedField {
        String fieldValue;
        long fieldVersion;

        public VersionedField(String fieldValue, long fieldVersion) {
            this.fieldValue = fieldValue;
            this.fieldVersion = fieldVersion;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public long getFieldVersion() {
            return fieldVersion;
        }

        public void setFieldVersion(long fieldVersion) {
            this.fieldVersion = fieldVersion;
        }
    }
}
