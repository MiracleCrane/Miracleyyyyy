/*
 * 文 件 名:  ExportCipherContent.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2023. All rights reserved.
 * 版    本:  SmartCampus R23.1
 * 描    述:  <描述>
 * 修 改 人:  z00569896
 * 修改时间： 2023/9/19
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.base.vo.cipher;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 导出密码箱，文本内容类
 *
 * @author z00569896
 * @version [SmartCampus R23.1, 2023/9/19]
 * @see [相关类/方法]
 * @since [SmartCampus R23.1]
 */
public class ExportCipherContent {
    private String cipherKey;
    private String cipherText;

    @JSONField(name = "cipher_key")
    public String getCipherKey() {
        return cipherKey;
    }

    public void setCipherKey(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    @JSONField(name = "cipher_text")
    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }
}