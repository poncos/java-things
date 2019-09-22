package com.ecollado.samples.openapi.api;

import com.ecollado.samples.openapi.model.FileInfo;
import com.ecollado.samples.openapi.model.FileList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class FilesApiDelegateImpl implements FilesApiDelegate {

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.empty();
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getAcceptHeader() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<Void> addFile(FileList body) {
        return null;
    }

    @Override
    public ResponseEntity<FileInfo> deleteFileById(String fileId) {
        return null;
    }

    @Override
    public ResponseEntity<List<FileList>> getFile(String fileId) {
        return null;
    }

    @Override
    public ResponseEntity<FileInfo> getFileById(String fileId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateFileById(FileList body) {
        return null;
    }
}
