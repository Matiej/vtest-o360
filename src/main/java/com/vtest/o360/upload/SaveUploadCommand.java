package com.vtest.o360.upload;


import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Builder
public class SaveUploadCommand {
    private String fileName;
    private byte[] file;
    private String contentType;
    private String thumbnailUri;

    @Override
    public String toString() {
        return "SaveUploadCommand{" +
                "fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", thumbnailUri='" + thumbnailUri + '\'' +
                '}';
    }
}
