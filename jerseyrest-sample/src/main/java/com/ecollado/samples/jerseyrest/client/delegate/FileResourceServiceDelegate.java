package com.ecollado.samples.jerseyrest.client.delegate;

import com.ecollado.samples.jerseyrest.client.api.FileResourceService;
import com.ecollado.samples.jerseyrest.common.model.FileResource;
import com.ecollado.samples.jerseyrest.common.model.FileResources;
import com.ecollado.samples.jerseyrest.exceptions.FileResourceServiceException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;


public class FileResourceServiceDelegate implements FileResourceService {

    private static final String JERSEY_REST_FILE_RESOURCES = "/jersey-rest-sample/api/files";

    @Override
    public List<FileResource> createFileResources(HostInformation hostInformation, List<FileResource> fileResources)
            throws FileResourceServiceException {

        if (hostInformation == null) {
            throw new IllegalArgumentException("Host information not set.");
        }

        RestClientUtils restClientUtils = new RestClientUtils();

        FileResources fileResourcesContainer = new FileResources();
        fileResourcesContainer.setFileResources(fileResources);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String content = objectMapper.writeValueAsString(fileResources);

            HttpResponse httpResponse = restClientUtils.doPost(hostInformation, JERSEY_REST_FILE_RESOURCES, content);

            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new FileResourceServiceException(
                        String.format("Error accessing FileResourceService with error message [%s]",
                                httpResponse.getStatusLine().getReasonPhrase()));
            }

            Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
            String responseString = EntityUtils.toString(httpResponse.getEntity(), contentEncoding.getValue());
            FileResources fileResourcesRsp = objectMapper.readValue(responseString, FileResources.class);

            return fileResourcesRsp.getFileResources();

        } catch (IOException e) {
            throw new FileResourceServiceException("Error accessing FileResourceService", e);
        }
    }

    @Override
    public List<FileResource> findFileResources(HostInformation hostInformation, String name)
            throws FileResourceServiceException {
        if (hostInformation == null) {
            throw new IllegalArgumentException("Host information not set.");
        }

        try {
            RestClientUtils restClientUtils = new RestClientUtils();

            HttpResponse httpResponse = restClientUtils.doGet(hostInformation, JERSEY_REST_FILE_RESOURCES);

            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new FileResourceServiceException(
                        String.format("Error accessing FileResourceService with error message [%s]",
                                httpResponse.getStatusLine().getReasonPhrase()));
            }

            ObjectMapper mapper = new ObjectMapper();

            String responseString = EntityUtils.toString(httpResponse.getEntity());
            FileResources fileResources = mapper.readValue(responseString, FileResources.class);

            return fileResources.getFileResources();

        } catch (IOException e) {
            throw new FileResourceServiceException("Error accessing FileResourceService", e);
        }
    }
}
