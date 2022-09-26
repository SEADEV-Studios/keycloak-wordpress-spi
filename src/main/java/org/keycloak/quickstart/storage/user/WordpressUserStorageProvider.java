/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * 
 * Original Author:
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 * 
 * Adapdation for Wordpress:
 * @author <a href="mailto:office@seadev-studios.com">SEADEV Studios GmbH</a>
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
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.keycloak.storage.user.UserRegistrationProvider;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordpressUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        UserRegistrationProvider,
        UserQueryProvider,
        CredentialInputUpdater,
        CredentialInputValidator,
        OnUserCache
{
    private String wordpressURL = "https://wordpress.develop.seadev-studios.com/wp-json/wp/v2";
    private String wordpressAuthorizationHash = "ZXh0ZXJuYWxBdXRoOjFnNFIwJm1UbHA4YkhmJDghdVU5MkNkeA==";

    private static final Logger logger = Logger.getLogger(WordpressUserStorageProvider.class);
    public static final String ID_CACHE_KEY = UserAdapter.class.getName() + ".id";    

    protected ComponentModel model;
    protected KeycloakSession session;
    protected JsonUrlReader jsonUrlReader;

    WordpressUserStorageProvider(KeycloakSession session, ComponentModel model) {
        this.session = session;
        this.model = model;

        this.jsonUrlReader = new JsonUrlReader(wordpressURL, new java.util.ArrayList<java.util.Map.Entry<String,String>>() {
            {
                add(new java.util.AbstractMap.SimpleEntry<String,String>("Authorization", "Basic "+wordpressAuthorizationHash));
            }
        });
    }

    @Override
    public void preRemove(RealmModel realm) {

    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {

    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {

    }

    @Override
    public void close() {
    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        logger.debug("getUserById: " + id);
        UserEntity entity = new UserEntity();
        try {
            String externalId = StorageId.externalId(id);
            JsonNode res = jsonUrlReader.get("/users/"+externalId+"?context=edit", "GET");
            entity.parseFromJsonNode(res);
        } catch (FileNotFoundException e) {
            // Can be ignored
            logger.info("User not found by id: " + id);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        UserAdapter ua = new UserAdapter(session, realm, model, entity);
        if (!ua.hasRole(KeycloakModelUtils.getRoleFromString(realm, "pro"))) {
            ua.grantRole(KeycloakModelUtils.getRoleFromString(realm, "pro"));
        }

        return ua;
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        logger.debug("getUserByUsername: " + username);
        
        UserEntity entity = new UserEntity();
        try {
            JsonNode res = jsonUrlReader.get("/users?context=edit&search="+username, "GET");
            if (res.size() > 0) {
                entity.parseFromJsonNode(res.get(0));
            }
        } catch (FileNotFoundException e) {
            // Can be ignored
            logger.info("User not found by username: " + username);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        UserAdapter ua = new UserAdapter(session, realm, model, entity);
        if (!ua.hasRole(KeycloakModelUtils.getRoleFromString(realm, "pro"))) {
            ua.grantRole(KeycloakModelUtils.getRoleFromString(realm, "pro"));
        }

        return ua;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        logger.debug("getUserByEmail: " + email);
        
        UserEntity entity = new UserEntity();
        try {
            JsonNode res = jsonUrlReader.get("/users?context=edit&search="+email, "GET");
            if (res.size() > 0) {
                entity.parseFromJsonNode(res.get(0));
            }
        } catch (FileNotFoundException e) {
            // Can be ignored
            logger.info("User not found by email: " + email);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        UserAdapter ua = new UserAdapter(session, realm, model, entity);
        if (!ua.hasRole(KeycloakModelUtils.getRoleFromString(realm, "pro"))) {
            ua.grantRole(KeycloakModelUtils.getRoleFromString(realm, "pro"));
        }

        return ua;
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {
        logger.debug("addUser: " + username);
        
        UserEntity entity = new UserEntity();
        try {
            String requestBody = "{\"username\":\""+username+"\",\"email\":\""+username+"\",\"name\":\""+username+"\"}";
            JsonNode res = jsonUrlReader.get("/users?context=edit", "POST", requestBody);
            if (res.size() > 0) {
                entity.parseFromJsonNode(res.get(0));
            }
        } catch (FileNotFoundException e) {
            // Can be ignored
            logger.info("Could not create user with username: " + username);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        UserAdapter ua = new UserAdapter(session, realm, model, entity);
        if (!ua.hasRole(KeycloakModelUtils.getRoleFromString(realm, "pro"))) {
            ua.grantRole(KeycloakModelUtils.getRoleFromString(realm, "pro"));
        }

        return ua;
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        logger.debug("not supported, removeUser: " + user.getUsername());
        return true;
    }

    @Override
    public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
        logger.debug("onCache: " + user.getUsername());
        String password = ((UserAdapter)delegate).getId();
        if (password != null) {
            user.getCachedWith().put(ID_CACHE_KEY, password);
        }
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        logger.debug("supportsCredentialType: " + credentialType);
        return credentialType == "password";
    }

    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        logger.debug("updateCredential: " + user.getUsername());
        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;
        UserCredentialModel cred = (UserCredentialModel)input;

        try {
            String requestBody = "{\"password\":\""+cred.getValue()+"\"}";
            jsonUrlReader.get("/users/"+user.getId(), "PUT", requestBody);
        } catch (FileNotFoundException e) {
            // Can be ignored
            logger.info("Could not update user with id: " + user.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public UserAdapter getUserAdapter(UserModel user) {
        logger.debug("getUserAdapter: " + user.getUsername());
        UserAdapter adapter = null;
        if (user instanceof CachedUserModel) {
            adapter = (UserAdapter)((CachedUserModel)user).getDelegateForUpdate();
        } else {
            adapter = (UserAdapter)user;
        }
        return adapter;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        logger.debug("not supported disableCredentialType: " + user.getUsername());
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        logger.debug("getDisableableCredentialTypes: " + user.getUsername());
        if (getUserAdapter(user).getId() != null) {
            Set<String> set = new HashSet<>();
            set.add("password");
            return set;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        logger.debug("isConfiguredFor: " + user.getUsername());
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        logger.debug("isValid: " + user.getUsername());

        if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) return false;

        // For validation we try to fetch the me user via API and the users credentials
        // Create the basic authentication string
        UserCredentialModel cred = (UserCredentialModel)input;
        String authString = user.getUsername() + ":" + cred.getValue();
        String authStringEnc = new String(Base64.getEncoder().encodeToString(authString.getBytes()));
        
        JsonUrlReader validationReader = new JsonUrlReader(wordpressURL, new java.util.ArrayList<java.util.Map.Entry<String,String>>() {
            {
                add(new java.util.AbstractMap.SimpleEntry<String,String>("Authorization", "Basic "+authStringEnc));
            }
        });

        try {
            validationReader.get("/users/me", "GET");
        } catch (IOException e) {
            logger.debug("User not valid");
            return false;
        }

        logger.debug("User is valid");
        return true;
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        logger.debug("getUsersCount");
        try {
            String usersCount = jsonUrlReader.getHeaderField("/users", "GET", "x-wp-total");
            return Integer.parseInt(usersCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;   
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return getUsers(realm, -1, -1);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        logger.debug("getUsers");
        List<UserModel> users = new LinkedList<>();

        try {
            Integer page = 1;
            Integer perPage = 10;

            if (maxResults > 0) {
                perPage = maxResults;
            }
            if (firstResult > 0) {
                page = firstResult / perPage;
            }
            
            JsonNode res = jsonUrlReader.get("/users?context=edit&page="+page+"&per_page="+perPage, "GET");
            for (JsonNode child : res) {
                UserEntity entity = new UserEntity();
                entity.parseFromJsonNode(child);

                UserAdapter ua = new UserAdapter(session, realm, model, entity);
                if (!ua.hasRole(KeycloakModelUtils.getRoleFromString(realm, "pro"))) {
                    ua.grantRole(KeycloakModelUtils.getRoleFromString(realm, "pro"));
                }

                users.add(ua);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return users;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return searchForUser(search, realm, -1, -1);
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        logger.debug("searchForUser: " + search);
        List<UserModel> users = new LinkedList<>();

        try {
            Integer page = 1;
            Integer perPage = 10;

            if (maxResults > 0) {
                perPage = maxResults;
            }
            if (firstResult > 0) {
                page = firstResult / perPage;
            }
            
            JsonNode res = jsonUrlReader.get("/users?context=edit&page="+page+"&per_page="+perPage+"&search="+search, "GET");
            for (JsonNode child : res) {
                UserEntity entity = new UserEntity();
                entity.parseFromJsonNode(child);

                UserAdapter ua = new UserAdapter(session, realm, model, entity);
                if (!ua.hasRole(KeycloakModelUtils.getRoleFromString(realm, "pro"))) {
                    ua.grantRole(KeycloakModelUtils.getRoleFromString(realm, "pro"));
                }

                users.add(ua);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return users;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        return new LinkedList<>();
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        return new LinkedList<>();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        return new LinkedList<>();
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        return new LinkedList<>();
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        return new LinkedList<>();
    }
}
