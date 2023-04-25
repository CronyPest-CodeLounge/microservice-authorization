package ru.skillbox.diplom.group35.microservice.authorization.impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.annotation.JwtProvider;
import ru.skillbox.diplom.group35.library.core.exception.UnauthorizedException;
import ru.skillbox.diplom.group35.library.core.security.config.SecurityConfig;
import ru.skillbox.diplom.group35.library.core.security.config.TechnicalUserConfig;
import ru.skillbox.diplom.group35.library.core.security.jwt.JwtTokenProvider;
import ru.skillbox.diplom.group35.microservice.account.api.client.AccountFeignClient;
import ru.skillbox.diplom.group35.microservice.account.api.dto.AccountDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.AuthenticateDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.AuthenticateResponseDto;
import ru.skillbox.diplom.group35.microservice.authorization.domain.model.Account;
import ru.skillbox.diplom.group35.microservice.authorization.impl.repository.AccountRepository;

@Service
@JwtProvider
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;

    private final AccountFeignClient accountFeignClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final TechnicalUserConfig technicalUserConfig;



    public AuthenticateResponseDto getAuthenticationResponse(AuthenticateDto authenticateDto) throws UnauthorizedException {
        AuthenticateResponseDto authenticateResponseDto = new AuthenticateResponseDto();
        ResponseEntity<AccountDto> responseEntity = technicalUserConfig.executeByTechnicalUser(()->accountFeignClient.getByEmail(authenticateDto.getEmail()));
        AccountDto accountDto = responseEntity.getBody();
        if (accountDto != null
                && bCryptPasswordEncoder.matches(authenticateDto.getPassword(), accountDto.getPassword())) {
            String jwtToken = jwtTokenProvider.createToken(accountDto.getId(), accountDto.getEmail());
            authenticateResponseDto.setAccessToken(jwtToken);
            authenticateResponseDto.setRefreshToken(jwtToken);
        }
        else {
            throw new UnauthorizedException("Incorrect email or password");
        }

        return authenticateResponseDto;
    }
}
