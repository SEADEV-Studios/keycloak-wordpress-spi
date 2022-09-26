/*
 * Copyright 2022 SEADEV Studios GmbH
 * 
 * @author <a href="mailto:contact@seadev-studios.com">SEADEV Studios GmbH</a>
 * @version $Revision: 1 $
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUrlReader {
    private String baseUrl = "";
    private java.util.List<java.util.Map.Entry<String,String>> headers = new java.util.ArrayList<java.util.Map.Entry<String,String>>();

    public JsonUrlReader(String baseUrl, java.util.List<java.util.Map.Entry<String,String>> headers) {
        this.baseUrl = baseUrl;
        this.headers = headers;
    }

    private HttpURLConnection createConnection(String url, String requestMethod, String requestBody) throws MalformedURLException, IOException, ProtocolException {
        URL obj = new URL(baseUrl + url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(requestMethod);
        for (java.util.Map.Entry<String,String> header : headers) {
            con.setRequestProperty(header.getKey(), header.getValue());
        }

        if (requestBody != null && !requestBody.isEmpty()) {
            con.setDoOutput(true);
            con.getOutputStream().write(requestBody.getBytes(Charset.forName("UTF-8")));
        }
        return con;
    }

    public JsonNode get(String url, String requestType) throws StreamReadException, DatabindException, MalformedURLException, IOException {
      return get(url, requestType, null);
    }

    public JsonNode get(String url, String requestType, String requestBody) throws StreamReadException, DatabindException, MalformedURLException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        HttpURLConnection con = createConnection(url, requestType, requestBody);

        JsonNode json = mapper.readTree(con.getInputStream());
        return json;
    }

    public String getHeaderField(String url, String requestType, String headerName) throws StreamReadException, DatabindException, MalformedURLException, IOException {
      URL obj = new URL(baseUrl + url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestMethod(requestType);
      return con.getHeaderField(headerName);
    }
}
