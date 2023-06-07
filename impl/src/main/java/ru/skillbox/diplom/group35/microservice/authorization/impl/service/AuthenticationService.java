package ru.skillbox.diplom.group35.microservice.authorization.impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.group35.library.core.annotation.JwtProvider;
import ru.skillbox.diplom.group35.library.core.exception.UnauthorizedException;
import ru.skillbox.diplom.group35.library.core.security.config.SecurityConfig;
import ru.skillbox.diplom.group35.library.core.security.config.TechnicalUserConfig;
import ru.skillbox.diplom.group35.library.core.security.jwt.JwtTokenProvider;
import ru.skillbox.diplom.group35.library.core.utils.SecurityUtil;
import ru.skillbox.diplom.group35.microservice.account.api.client.AccountFeignClient;
import ru.skillbox.diplom.group35.microservice.account.api.dto.AccountSecureDto;
import ru.skillbox.diplom.group35.microservice.account.domain.model.Authority;
import ru.skillbox.diplom.group35.microservice.account.domain.model.Role;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.AuthenticateDto;
import ru.skillbox.diplom.group35.microservice.authorization.api.dto.AuthenticateResponseDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@JwtProvider
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AccountFeignClient accountFeignClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TechnicalUserConfig technicalUserConfig;
    private final  SecurityConfig securityConfig;
    private final SecurityUtil securityUtil;



    public AuthenticateResponseDto getAuthenticationResponse(AuthenticateDto authenticateDto) throws UnauthorizedException {
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
            List<String> authorities =  accountSecureDto.getAuthorities()
                    .stream()
                    .map(Authority::getAuthority)
                    .collect(Collectors.toList());
            roles.addAll(authorities);
            String jwtToken = jwtTokenProvider.createToken(
                    accountSecureDto.getId(),
                    accountSecureDto.getEmail(),
                   roles);
            String refreshJwtToken = jwtTokenProvider.refreshToken(accountSecureDto.getId(), accountSecureDto.getEmail());
            authenticateResponseDto.setAccessToken(jwtToken);
            authenticateResponseDto.setRefreshToken(refreshJwtToken);
        }
        else {
            throw new UnauthorizedException("Incorrect email or password");
        }
        return authenticateResponseDto;
    }

    public AuthenticateResponseDto refreshToken(String refreshToken) {
        UUID id = UUID.fromString(securityConfig.jwtDecoder().decode(refreshToken).getClaims().get("id").toString());
        String email= (String) securityConfig.jwtDecoder().decode(refreshToken).getClaims().get("email");
        if (!securityUtil.getAccountDetails().getId().equals(id)
                && !securityUtil.getAccountDetails().getEmail().equals(email)){
            throw new UnauthorizedException("invalid refresh token");
        }
        AuthenticateResponseDto authenticateResponseDto = new AuthenticateResponseDto();
        authenticateResponseDto.setRefreshToken(jwtTokenProvider.refreshToken(id, email));
        authenticateResponseDto.setAccessToken(jwtTokenProvider.createToken(id, email, securityUtil.getAccountDetails().getRoles()));
       return authenticateResponseDto;
    }
}
