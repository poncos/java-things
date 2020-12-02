package com.ecollado.samples.jerseyrest;

import com.ecollado.samples.jerseyrest.client.api.FileResourceService;
import com.ecollado.samples.jerseyrest.client.delegate.FileResourceServiceDelegate;
import com.ecollado.samples.jerseyrest.client.delegate.HostInformation;
import com.ecollado.samples.jerseyrest.common.model.FileResource;
import com.ecollado.samples.jerseyrest.exceptions.FileResourceServiceException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class FileResourceServiceClientTestCases {


    @Test
    public void fileResourceServiceGetTestCase() {
        FileResourceService fileResourceService = new FileResourceServiceDelegate();

        HostInformation hostInformation = new HostInformation("localhost", 8090);

        try {
            List<FileResource> fileResources = fileResourceService.findFileResources(hostInformation, null);

            System.out.println(fileResources.stream().map(item -> item.toString()).
                    collect(Collectors.joining(",")));

        } catch (FileResourceServiceException e) {
            Assert.fail();
        }
    }

    @Test
    public void fileResourceServicePostTestCase() {
        FileResourceService fileResourceService = new FileResourceServiceDelegate();

        HostInformation hostInformation = new HostInformation("localhost", 8090);

        try {
            List<FileResource> fileResources = fileResourceService.findFileResources(hostInformation, null);

            System.out.println(fileResources.stream().map(item -> item.toString()).
                    collect(Collectors.joining(",")));

        } catch (FileResourceServiceException e) {
            Assert.fail();
        }
    }
}
