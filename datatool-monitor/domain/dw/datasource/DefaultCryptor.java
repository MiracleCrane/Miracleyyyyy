/*
 * 文 件 名:  DefaultCryptor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

import com.huawei.smartcampus.datatool.utils.DataToolCryptor;

/**
 * DataTool提供的统一解密能力
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/28]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public class DefaultCryptor implements DWCryptor {
    @Override
    public String decrypt(String encrypted) {
        return DataToolCryptor.decodingSecret(encrypted);
    }
}