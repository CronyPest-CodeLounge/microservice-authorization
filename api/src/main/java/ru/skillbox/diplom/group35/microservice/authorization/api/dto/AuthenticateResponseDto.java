package ru.skillbox.diplom.group35.microservice.authorization.api.dto;

import lombok.AllArgsConstructor;
import ru.skillbox.diplom.group35.library.core.dto.base.BaseDto;

/**
 * AuthenticateResponseDto
 *
 * @author Mikhail Chechetkin
 */

@AllArgsConstructor
public class AuthenticateResponseDto extends BaseDto {
    private String accessToken;
    private String refreshToken;
}
