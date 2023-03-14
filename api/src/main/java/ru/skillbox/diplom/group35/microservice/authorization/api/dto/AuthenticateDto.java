package ru.skillbox.diplom.group35.microservice.authorization.api.dto;

import lombok.AllArgsConstructor;

/**
 * AuthenticateDto
 *
 * @author Mikhail Chechetkin
 */

@AllArgsConstructor
public class AuthenticateDto {
    private String email;
    private String password;
}
