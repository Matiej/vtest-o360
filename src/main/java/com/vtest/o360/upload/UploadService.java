package com.vtest.o360.upload;

import java.util.Optional;

public interface UploadService {

    Optional<UploadResponse> getUploadById(Long id);
    UploadResponse save(SaveUploadCommand command);
    void remove(Long id);


}
