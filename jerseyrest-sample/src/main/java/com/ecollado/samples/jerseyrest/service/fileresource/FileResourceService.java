package com.ecollado.samples.jerseyrest.service.fileresource;

import com.ecollado.samples.jerseyrest.common.model.FileResource;
import com.ecollado.samples.jerseyrest.common.model.FileResources;

import javax.ws.rs.core.Response;

public interface FileResourceService {

    FileResources getFiles(final String fileName);

    FileResource createFile(FileResource fileResource);

    FileResource updateFile(final String fileId, FileResource fileResource);

    Response deleteFile(final String id);
}
