/*
 * 文 件 名:  DataToolProtocol.java
 * 版    权:  Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 * 版    本:  SmartCampus V100R001C00
 * 描    述:  <描述>
 * 修 改 人:  l30006786
 * 修改时间： 2021/6/1
 * 修改内容:  <新增>
 */

package com.huawei.smartcampus.datatool.util;

import com.huawei.seccomponent.crypt.CryptoAPI;
import com.huawei.seccomponent.crypt.CryptoFactory;

import org.apache.coyote.http11.Http11NioProtocol;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author l30006786
 * @version [SmartCampus V100R001C00, 2021/6/1]
 * @see [相关类/方法]
 * @since [SmartCampus V100R001C00]
 */
public class DataToolHttp11Protocol extends Http11NioProtocol {
    /**
     * 解密证书
     *
     * @param certificateKeystorePwd certificateKeystorePwd
     */
    public void setKeystorePass(String certificateKeystorePwd) {
        try {
            CryptoAPI api = CryptoFactory.getInstance(System.getenv("SCC_CONF"));
            super.setKeystorePass(api.decrypt(certificateKeystorePwd).getString());
        } catch (Exception exception) {
            super.getLog().error("decrypt certificateKeystorePassword fail. please check");
        }
        // pkcs12证书指定加密算法后，需要同步修改加载证书的逻辑，否则启动失败
        Security.addProvider(new BouncyCastleProvider());
        super.setKeystoreProvider(BouncyCastleProvider.PROVIDER_NAME);
    }
}
