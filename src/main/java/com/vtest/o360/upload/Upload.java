package com.vtest.o360.upload;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@Builder
public class Upload {
    private Long id;
    private String fileName;
    private byte[] file;
    private String contentType;
    private LocalDateTime createdAt;
    private String path;

    @Override
    public String toString() {
        return "Upload{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", createdAt=" + createdAt +
                ", path='" + path + '\'' +
                '}';
    }
}
