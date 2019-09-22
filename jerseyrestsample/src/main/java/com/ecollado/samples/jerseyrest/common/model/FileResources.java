package com.ecollado.samples.jerseyrest.common.model;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "files")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filesType")
public class FileResources {

    @XmlElement(name = "files")
    @JsonProperty("files")
    private List<FileResource> fileResources = new ArrayList<FileResource>();


    public List<FileResource> getFileResources() {
        return fileResources;
    }

    public void setFileResources(List<FileResource> fileResources) {
        this.fileResources = fileResources;
    }

    @Override
    public String toString() {
        return "FileResources{" +
                "fileResources=" + fileResources +
                '}';
    }
}
