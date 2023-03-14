package ru.skillbox.diplom.group35.microservice.authorization.api.dto;

import lombok.AllArgsConstructor;

/**
 * CaptchaDto
 *
 * @author Mikhail Chechetkin
 */

@AllArgsConstructor
public class CaptchaDto {
    private String secret;
    private String image;
}
