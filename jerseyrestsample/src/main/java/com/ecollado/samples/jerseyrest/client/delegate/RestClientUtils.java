package com.ecollado.samples.jerseyrest.client.delegate;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class RestClientUtils {

    public HttpResponse doGet(HostInformation hostInformation, String path) throws IOException {

        Client client = Client.create();

        String fullRestAddress = String.format("%s://%s:%d%s",
                hostInformation.getScheme(), hostInformation.getHost(), hostInformation.getPort(), path );

        WebResource webResource = client.resource(fullRestAddress);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);

        return this.getResponse(response);
    }

    public HttpResponse doPost(HostInformation hostInformation, String path, String content) throws IOException {
        Client client = Client.create();

        String fullRestAddress = String.format("%s://%s:%d/%s",
                hostInformation.getScheme(), hostInformation.getHost(), hostInformation.getPort(), path );

        WebResource webResource = client.resource(fullRestAddress);

        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, content);


        return this.getResponse(response);
    }

    private HttpResponse getResponse(ClientResponse clientResponse) throws IOException {

        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse response = factory.newHttpResponse(
                new BasicStatusLine(
                        HttpVersion.HTTP_1_1,
                        clientResponse.getStatus(),
                        clientResponse.getStatusInfo().getReasonPhrase()), null);

        for (Map.Entry<String, List<String>> entry : clientResponse.getHeaders().entrySet()) {
            for (String value : entry.getValue()) {
                response.setHeader(entry.getKey(), value);
            }
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(clientResponse.getEntityInputStream()));

        String line = "";
        StringBuffer buffer = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            buffer.append(line);
        }

        response.setEntity(new StringEntity(buffer.toString()));
        return response;
    }

}
