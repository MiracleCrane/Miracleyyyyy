/*
 * 文 件 名:  DatatoolCryptor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2021/2/9
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.utils;

import com.huawei.seccomponent.common.SCException;
import com.huawei.seccomponent.crypt.CryptoAPI;
import com.huawei.seccomponent.crypt.CryptoFactory;
import com.huawei.smartcampus.datatool.exception.DataToolRuntimeException;
import com.huawei.smartcampus.datatool.exception.code.ExceptionCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用公司SCC解密sdk，实现解密
 *
 * @author z00569896
 * @version [SmartCampus V100R001C00, 2021/2/9]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public final class DataToolCryptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataToolCryptor.class);
    private static CryptoAPI api;

    static {
        try {
            api = CryptoFactory.getInstance(System.getenv("SCC_CONF"));
        } catch (SCException e) {
            LOGGER.error("initialize scc error.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DECODE_ERROR);
        }
    }

    private DataToolCryptor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取解密的值
     *
     * @param encodingSecret 需要解密的参数
     * @return 解密后的参数值
     */
    public static String decodingSecret(String encodingSecret) {
        try {
            return api.decrypt(encodingSecret).getString();
        } catch (Exception exception) {
            LOGGER.error("decrypt data failed.");
            throw new DataToolRuntimeException(ExceptionCode.DATATOOL_DECODE_ERROR);
        }
    }
}
