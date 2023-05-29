package ru.skillbox.diplom.group35.microservice.authorization.impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.annotation.JwtProvider;
import ru.skillbox.diplom.group35.library.core.exception.UnauthorizedException;
import ru.skillbox.diplom.group35.library.core.security.config.TechnicalUserConfig;
import ru.skillbox.diplom.group35.library.core.security.jwt.JwtTokenProvider;
import ru.skillbox.diplom.group35.microservice.account.api.client.AccountFeignClient;
import ru.skillbox.diplom.group35.microservice.account.api.dto.AccountSecureDto;
import ru.skillbox.diplom.group35.microservice.account.domain.model.Role;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.AuthenticateDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.AuthenticateResponseDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@JwtProvider
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountFeignClient accountFeignClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TechnicalUserConfig technicalUserConfig;



    public AuthenticateResponseDto getAuthenticationResponse(AuthenticateDto authenticateDto, HttpServletResponse response) throws UnauthorizedException {
        AuthenticateResponseDto authenticateResponseDto = new AuthenticateResponseDto();
        ResponseEntity<AccountSecureDto> responseEntity = technicalUserConfig.executeByTechnicalUser(
                ()->accountFeignClient.getByEmail(authenticateDto.getEmail()));
        AccountSecureDto accountSecureDto = responseEntity.getBody();
        if (accountSecureDto != null
                && bCryptPasswordEncoder.matches(authenticateDto.getPassword(), accountSecureDto.getPassword())) {
            List<String> roles = accountSecureDto.getRoles()
                    .stream()
                    .map(Role::getRole)
                    .collect(Collectors.toList());
            String jwtToken = jwtTokenProvider.createToken(
                    accountSecureDto.getId(),
                    accountSecureDto.getEmail(),
                   roles);
            authenticateResponseDto.setAccessToken(jwtToken);
            authenticateResponseDto.setRefreshToken(jwtToken);
            response.addCookie(createCookie(jwtToken));
        }
        else {
            throw new UnauthorizedException("Incorrect email or password");
        }

        return authenticateResponseDto;
    }

    private Cookie createCookie(String jwtToken) {
        Cookie cookie = new Cookie("jwtPath", jwtToken);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
