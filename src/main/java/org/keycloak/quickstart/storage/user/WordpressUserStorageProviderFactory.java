/*
 * Copyright 2022 SEADEV Studios GmbH
 * 
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * 
 * Original Author:
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 * 
 * Adapdation for Wordpress:
 * @author <a href="mailto:contact@seadev-studios.com">SEADEV Studios GmbH</a>
 * @version $Revision: 2 $
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.quickstart.storage.user;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;

public class WordpressUserStorageProviderFactory implements UserStorageProviderFactory<WordpressUserStorageProvider> {
    public static final String PROVIDER_ID = "wordpress-user-storage-spi";

    private static final Logger logger = Logger.getLogger(WordpressUserStorageProviderFactory.class);

    @Override
    public WordpressUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        return new WordpressUserStorageProvider(session, model);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return "Wordpress User Storage Provider";
    }

    @Override
    public void close() {
        logger.info("Closing WordpressUserStorageProviderFactory");
    }
}
