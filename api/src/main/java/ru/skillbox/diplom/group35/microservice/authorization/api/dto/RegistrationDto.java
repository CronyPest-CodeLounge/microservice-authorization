package ru.skillbox.diplom.group35.microservice.authorization.api.dto;

import lombok.AllArgsConstructor;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;

/**
 * RegistrationDto
 *
 * @author Mikhail Chechetkin
 */

@AllArgsConstructor
public class RegistrationDto extends BaseDto {
    private String email;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String captchaCode;
    private String captchaSecret;
}