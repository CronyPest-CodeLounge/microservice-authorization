package ru.skillbox.diplom.group35.microservice.authorization.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class KafkaDto {
    private String email;
    private ZonedDateTime registrationDate;
    private boolean isSuccessful;
    private String error;

    public KafkaDto(String email, ZonedDateTime registrationDate) {
        this.email = email;
        this.registrationDate = registrationDate;
    }
}
