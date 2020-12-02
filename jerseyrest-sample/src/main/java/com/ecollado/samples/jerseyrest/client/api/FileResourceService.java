package com.ecollado.samples.jerseyrest.client.api;

import com.ecollado.samples.jerseyrest.client.delegate.HostInformation;
import com.ecollado.samples.jerseyrest.common.model.FileResource;
import com.ecollado.samples.jerseyrest.exceptions.FileResourceServiceException;

import java.util.List;

public interface FileResourceService {

    List<FileResource> createFileResources(HostInformation hostInformation, List<FileResource> fileResources)
            throws FileResourceServiceException;

    List<FileResource> findFileResources(HostInformation hostInformation, String name)
            throws FileResourceServiceException;
}
