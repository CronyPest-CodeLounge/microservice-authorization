package ru.skillbox.diplom.group35.microservice.authorization.impl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.security.config.TechnicalUserConfig;
import ru.skillbox.diplom.group35.microservice.account.api.client.AccountFeignClient;
import ru.skillbox.diplom.group35.microservice.account.api.dto.AccountDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.KafkaDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.RegistrationDto;
import ru.skillbox.diplom.group35.microservice.authorization.impl.mapper.RegistrationMapper;

import java.time.ZonedDateTime;

/**
 * RegistrationService
 *
 * @author Mikhail Chechetkin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationMapper registrationMapper;
    private final AccountFeignClient accountFeignClient;
    private final CaptchaService captchaService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TechnicalUserConfig technicalUserConfig;
    private final KafkaProducerService kafkaProducerService;

    public void create(RegistrationDto dto) {
        AccountDto accountDto = registrationMapper.mapToAccount(dto);
        KafkaDto kafkaRegistrationDto = new KafkaDto(dto.getEmail(), ZonedDateTime.now());
        accountDto.setIsDeleted(false);
        accountDto.setCreatedOn(ZonedDateTime.now());
        accountDto.setUpdatedOn(ZonedDateTime.now());

        if (dto.getPassword1().equals(dto.getPassword2())
                && captchaService.captchaValidation(dto)
        ) {
            accountDto.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
            try {
                technicalUserConfig.executeByTechnicalUser(() -> accountFeignClient.create(accountDto));
                kafkaRegistrationDto.setSuccessful(true);
                kafkaProducerService.sendRegistration(kafkaRegistrationDto);
            } catch (Exception e) {
                log.warn(e.getMessage());
                kafkaRegistrationDto.setSuccessful(false);
                kafkaRegistrationDto.setError(e.getMessage());
                kafkaProducerService.sendRegistration(kafkaRegistrationDto);
                throw new RuntimeException(e.getMessage());

            }

        }
    }
}
