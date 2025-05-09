package com.bookmyshow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User details used in API")
public class UserDTO {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User's full name", example = "Alice Johnson")
    private String name;

    @Schema(description = "User's email address", example = "alice@example.com")
    private String email;

    @Schema(description = "User's role", example = "USER")
    private String role;

}
