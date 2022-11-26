package com.vtest.o360.user;

import com.vtest.o360.global.HeaderKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;

import static com.vtest.o360.global.HttpHeaderFactory.getSuccessfulHeaders;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users API", description = "API designed to manipulate the object user.")
public class UserController {
    private final UserService userService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user by ID from data base")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search successful"),
            @ApiResponse(responseCode = "404", description = "Server has not found anything matching the requested URI! No users found!"),
    })
    ResponseEntity<?> getById(@RequestParam("id")@NotNull(message = "File id can't be empty or null")
                              @Min(value = 1, message = "File id  must be greater then 0")  Long id) {
        return userService.getById(id).map(recipient -> ResponseEntity.ok()
                .headers(getSuccessfulHeaders(HttpStatus.OK, HttpMethod.GET))
                .body(recipient))
                .orElse(ResponseEntity.notFound()
                        .header(HeaderKey.STATUS.getHeaderKeyLabel(), HttpStatus.NOT_FOUND.name())
                        .header(HeaderKey.MESSAGE.getHeaderKeyLabel(), "User with ID: " + id + " not found!")
                        .build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register new user", description = "Add and register new user. All fields are validated")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User object created successful"),
            @ApiResponse(responseCode = "400", description = "Validation failed. Some fields are wrong. Response contains all details.")
    })
    ResponseEntity<Void> register(@Valid @RequestBody RestRegisterUser command) {
        //no validation. No tame or password validation
        log.info("Received request to register user: " + command);
        RegisterUserResponse registerUserResponse = userService.register(command.toCommand());

        if (!registerUserResponse.isSuccess()) {
            return ResponseEntity.badRequest()
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HttpMethod.POST.name())
                    .header(HeaderKey.STATUS.getHeaderKeyLabel(), HttpStatus.BAD_REQUEST.name())
                    .header(HeaderKey.MESSAGE.getHeaderKeyLabel(), registerUserResponse.getErrorMessage())
                    .build();
        }
        return ResponseEntity.created(getUri(registerUserResponse.getId()))
                .headers(getSuccessfulHeaders(HttpStatus.CREATED, HttpMethod.POST))
                .build();
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
