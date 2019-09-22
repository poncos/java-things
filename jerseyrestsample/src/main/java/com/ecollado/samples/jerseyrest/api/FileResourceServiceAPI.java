package com.ecollado.samples.jerseyrest.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ecollado.samples.jerseyrest.common.model.FileResource;
import com.ecollado.samples.jerseyrest.common.model.FileResources;
import com.ecollado.samples.jerseyrest.service.fileresource.FileResourceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/files")
@Component
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA })
public final class FileResourceServiceAPI implements FileResourceService {

    private static final Logger LOGGER = LogManager.getLogger(FileResourceServiceAPI.class);

    @GET
    @ResponseBody
    public FileResources getFiles(@QueryParam(value = "name") final String fileName) {
        LOGGER.info("getFiles with filter [{}]", fileName);
        FileResource fileResource = new FileResource();
        fileResource.setId(UUID.randomUUID().toString());
        fileResource.setDescription("file description");
        fileResource.setName("file Name");
        fileResource.setContent("This is a content");
        FileResources fileResources = new FileResources();

        List<FileResource> files = new ArrayList();
        files.add(fileResource);
        fileResources.setFileResources(files);
        return fileResources;
    }

    @GET
    @Path("/{id}")
    @ResponseBody
    public FileResources getFileById(@QueryParam(value = "name") final String fileName) {
        LOGGER.info("getFiles with filter [{}]", fileName);
        FileResource fileResource = new FileResource();
        fileResource.setId(UUID.randomUUID().toString());
        fileResource.setDescription("file description");
        fileResource.setName("file Name");
        fileResource.setContent("This is a content");
        FileResources fileResources = new FileResources();

        List<FileResource> files = new ArrayList();
        files.add(fileResource);
        fileResources.setFileResources(files);
        return fileResources;
    }

    @POST
    @ResponseBody
    public FileResource createFile(FileResource fileResource) {
        if (fileResource == null) {
            throw new IllegalArgumentException(
                    "The file resource is not set.");
        }

        fileResource.setId(UUID.randomUUID().toString());
        return fileResource;
    }

    @PUT
    @Path("/{id}")
    @ResponseBody
    public FileResource updateFile(@PathParam(value="id") final String fileId, FileResource fileResource) {

        return fileResource;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFile(@PathParam(value = "id") final String id) {

        return Response.ok().build();
    }
}
