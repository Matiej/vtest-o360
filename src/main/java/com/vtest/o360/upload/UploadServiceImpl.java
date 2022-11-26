package com.vtest.o360.upload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    private final String USER_DIR = "user.dir";
    private Map<Long, Upload> uploadTempDataBase = new HashMap<>();

    @Override
    public Optional<UploadResponse> getUploadById(Long id) {
        Upload optionalUplad = uploadTempDataBase.get(id);
        return Optional.ofNullable(optionalUplad).map(UploadResponse::toResponse);
    }

    @Override
    public UploadResponse save(SaveUploadCommand command) {
        //logging a lot of info
        log.info("Saving user in to data base: " + command);
        Upload upload = Upload.builder()
                .fileName(command.getFileName())
                .file(command.getFile())
                .contentType(command.getContentType())
                .createdAt(LocalDateTime.now())
                .path(System.getProperty(USER_DIR))
                .build();

        Upload savedUpload = saveToDataBase(upload);
        log.info("File saved successful: " + savedUpload);

        return UploadResponse.toResponse(savedUpload);
    }

    @Override
    public void remove(Long id) {
        //empty try catch  - check
        try {
            uploadTempDataBase.remove(id);
        } catch (IllegalArgumentException e) {

        }
    }

    private Upload saveToDataBase(Upload upload) {
        //print stack trace - to check if it shows itt
        Long uploadId = uploadTempDataBase.size() + 1L;
        try {
            upload.setId(uploadId);
            uploadTempDataBase.put(uploadId, upload);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            log.error("Something happened and finally block works");
        }
        return upload;
    }
}
