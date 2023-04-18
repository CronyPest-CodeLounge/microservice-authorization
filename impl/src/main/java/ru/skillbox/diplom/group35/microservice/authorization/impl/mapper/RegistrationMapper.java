package ru.skillbox.diplom.group35.microservice.authorization.impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group35.microservice.account.api.dto.AccountDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.RegistrationDto;
import ru.skillbox.diplom.group35.microservice.authorization.domain.model.Account;

/**
 * RegistrationMapper
 *
 * @author Mikhail Chechetkin
 */

@Mapper
public interface RegistrationMapper {

    @Mapping(source = "password1", target = "password")
    AccountDto mapToAccount(RegistrationDto registrationDto);

    @Mapping(source = "password", target = "password1")
    RegistrationDto mapToDto(Account account);
}
