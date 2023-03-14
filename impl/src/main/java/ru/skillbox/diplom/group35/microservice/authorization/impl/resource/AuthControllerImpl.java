package ru.skillbox.diplom.group35.microservice.authorization.impl.resource;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthorizationController
 *
 * @author Mikhail Chechetkin
 */

@RestController
@AllArgsConstructor

public class AuthControllerImpl {

    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<Object> register() {
        return null;
    }

    @PostMapping("/api/v1/auth/password/recovery")
    public ResponseEntity<Object> passwordRecovery() {
        return null;
    }

    @PostMapping("/api/v1/auth/password/recovery/{linkId}")
    public ResponseEntity<Object> newPassword(
            @RequestParam(name = "linkId", defaultValue = "") String linkId) {
        return null;
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<Object> login() {
        return null;
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<Object> logout() {
        return null;
    }

    @GetMapping("/api/v1/auth/captcha")
    public ResponseEntity<Object> captcha() {
        return null;
    }
}
