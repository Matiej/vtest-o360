package com.vtest.o360.upload;

import com.vtest.o360.global.HeaderKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.net.URI;

import static com.vtest.o360.global.HttpHeaderFactory.getSuccessfulHeaders;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/uploads")
@Tag(name = "Uploads API", description = "Upload file, max size 2MB")
public class UploadController {
    private final UploadService uploadService;


    @GetMapping(value = "/{id}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE,
                    MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            consumes = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE,
                    MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Operation(summary = "Get file by id", description = "Get file by id")
    @Parameter(name = "id", required = true, description = "Searching file ID")
    @Schema(example = "picture file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search successful"),
            @ApiResponse(responseCode = "404", description = "Server has not found anything matching the requested URI! No file found!"),
    })
    ResponseEntity<?> getUploadById(@PathVariable("id") @NotNull(message = "File id can't be empty or null")
                                         @Min(value = 1, message = "File id  must be greater then 0") Long id) {
        return uploadService.getUploadById(id)
                .map(file -> ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType(file.getContentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName:\"" + file.getOriginFileName() + "\"")
                        .header(HeaderKey.SERVER_FILENAME.getHeaderKeyLabel(), file.getServerFileName())
                        .header(HeaderKey.CREATED_AT.getHeaderKeyLabel(), file.getCreatedAt().toString())
                        .headers(getSuccessfulHeaders(HttpStatus.OK, HttpMethod.GET))
                        .body(new ByteArrayResource(file.getFile())))
                .orElseGet(() -> {
                    String message = "File with ID: '" + id + "' not found";
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HeaderKey.STATUS.getHeaderKeyLabel(), HttpStatus.NOT_FOUND.name())
                            .header(HeaderKey.MESSAGE.getHeaderKeyLabel(), message)
                            .build();
                });
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Save file", description = "Save file max 3mb")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "File saved successful"),
            @ApiResponse(responseCode = "400", description = "Validation failed. Some fields are wrong. Response contains all details."),
    })
    ResponseEntity<Void> save(@RequestParam(value = "file") MultipartFile file) throws IOException {
        log.info("Received request with file: " + file.getOriginalFilename());
        //no file name and file type validation. No no file name Sanitizes . directly save no validation
        UploadResponse response = uploadService.save(SaveUploadCommand
                .builder()
                .file(file.getBytes())
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build());
        return ResponseEntity.created(getUri(response.getId()))
                .headers(getSuccessfulHeaders(HttpStatus.CREATED, HttpMethod.POST))
                .build();

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove  picture", description = " picture byID")
    @Parameter(name = "id", required = true, description = "Removing file by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removed successful"),
    })
    void deleteCoverByBookId(@PathVariable("id") @NotNull(message = "Id filed can't be null")
                             @Min(value = 1, message = "Id field value must be greater than 0") Long id) {
        uploadService.remove(id);
    }

    private static URI getUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/users")
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }

}
