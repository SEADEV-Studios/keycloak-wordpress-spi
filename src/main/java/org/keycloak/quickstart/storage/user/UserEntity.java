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
import com.fasterxml.jackson.databind.JsonNode;

public class UserEntity {
    private static final Logger logger = Logger.getLogger(UserEntity.class);
    private String id;
    private String username;
    private String name;
    private String firstName;
    private String lastName;
    private String email;

    public void parseFromJsonNode(JsonNode node) {
        id = node.get("id").asText();
        username = node.get("username").asText();
        name = node.get("name").asText();
        firstName = node.get("first_name").asText();
        lastName = node.get("last_name").asText();
        email = node.get("email").asText();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
