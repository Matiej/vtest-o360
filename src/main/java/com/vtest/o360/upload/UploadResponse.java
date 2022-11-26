package com.vtest.o360.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UploadResponse {
    private Long id;
    private String originFileName;
    private String serverFileName;
    private final String contentType;
    @JsonIgnore
    private byte[] file;
    private LocalDateTime createdAt;
    private String path;

    public static UploadResponse toResponse(Upload upload) {
        return UploadResponse.builder()
                .id(upload.getId())
                .originFileName(upload.getFileName())
                .originFileName(upload.getFileName())
                .contentType(upload.getContentType())
                .createdAt(upload.getCreatedAt())
                .path(upload.getPath())
                .file(upload.getFile())
                .build();
    }
}
