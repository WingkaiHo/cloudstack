/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cloudstack.ldap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class LdapUserManagerFactory implements ApplicationContextAware {


    public static final Logger s_logger = Logger.getLogger(LdapUserManagerFactory.class.getName());

    private static Map<LdapUserManager.Provider, LdapUserManager> ldapUserManagerMap = new HashMap<>();

    static ApplicationContext applicationCtx;

    public LdapUserManager getInstance(LdapUserManager.Provider provider) {
        LdapUserManager ldapUserManager;
        if (provider == LdapUserManager.Provider.MICROSOFTAD) {
            ldapUserManager = ldapUserManagerMap.get(LdapUserManager.Provider.MICROSOFTAD);
            if (ldapUserManager == null) {
                ldapUserManager = new ADLdapUserManagerImpl();
                applicationCtx.getAutowireCapableBeanFactory().autowireBeanProperties(ldapUserManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
                ldapUserManagerMap.put(LdapUserManager.Provider.MICROSOFTAD, ldapUserManager);
            }
        } else {
            //defaults to opendldap
            ldapUserManager = ldapUserManagerMap.get(LdapUserManager.Provider.OPENLDAP);
            if (ldapUserManager == null) {
                ldapUserManager = new OpenLdapUserManagerImpl();
                applicationCtx.getAutowireCapableBeanFactory().autowireBeanProperties(ldapUserManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
                ldapUserManagerMap.put(LdapUserManager.Provider.OPENLDAP, ldapUserManager);
            }
        }
        return ldapUserManager;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationCtx = applicationContext;
    }
}
