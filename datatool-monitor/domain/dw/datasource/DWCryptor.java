/*
 * 文 件 名:  DWCryptor.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2023-2023. All rights reserved.
 * 版    本:  Core&Link 23.1
 * 描    述:  <描述>
 * 修 改 人:  g00560618
 * 修改时间： 2023/11/23
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.monitor.domain.dw.datasource;

/**
 * 提供密码解密能力
 *
 * @author g00560618
 * @version [Core&Link 23.1, 2023/11/28]
 * @see [相关类/方法]
 * @since [Core&Link 23.1]
 */
public interface DWCryptor {
    /**
     * 密文界面方法接口
     *
     * @param encrypted 密文
     * @return 解密后明文
     */
    String decrypt(String encrypted);
}