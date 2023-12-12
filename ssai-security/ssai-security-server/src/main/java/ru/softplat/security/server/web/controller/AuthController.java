package ru.softplat.security.server.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.softplat.main.dto.validation.New;
import ru.softplat.main.dto.validation.Update;
import ru.softplat.security.server.config.JwtTokenProvider;
import ru.softplat.security.server.dto.JwtAuthRequest;
import ru.softplat.security.server.dto.RestorePasswordRequestDto;
import ru.softplat.security.server.dto.UserCreateDto;
import ru.softplat.security.server.exception.WrongConditionException;
import ru.softplat.security.server.mapper.UserMapper;
import ru.softplat.security.server.message.ExceptionMessage;
import ru.softplat.security.server.message.LogMessage;
import ru.softplat.security.server.model.Role;
import ru.softplat.security.server.model.User;
import ru.softplat.security.server.repository.UserRepository;
import ru.softplat.security.server.service.AuthService;
import ru.softplat.security.server.service.EmailService;
import ru.softplat.security.server.service.UserDetailsChangeService;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserDetailsChangeService userDetailsChangeService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final EmailService emailService;


    @PostMapping("/auth/login")
    public ResponseEntity<Object> authorization(@RequestBody @Validated(Update.class) JwtAuthRequest request) {
        log.info(LogMessage.TRY_AUTHORIZATION.label, request.getEmail());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Map<Object, Object> response = new HashMap<>();
            User user = repository.findByEmail(request.getEmail()).orElseThrow(() ->
                    new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
            String token = jwtTokenProvider.generateToken(request.getEmail(), user.getRole().name());
            response.put("id", user.getIdMain());
            response.put("email", user.getEmail());
            response.put("token", token);
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(ExceptionMessage.INVALID_AUTHENTICATION.label, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/auth/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info(LogMessage.TRY_LOGOUT.label);

        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/registration")
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info(LogMessage.TRY_REGISTRATION.label);

        if ((userCreateDto.getRole().equals(Role.SELLER) || userCreateDto.getRole().equals(Role.BUYER)) &&
                (userCreateDto.getPhone() == null || userCreateDto.getPhone().isEmpty()))
            throw new WrongConditionException("Необходимо указать номер телефона. " +
                    "Телефонный номер должен начинаться с +7, затем - 10 цифр.");

        ResponseEntity<Object> response = authService.createNewUser(userCreateDto);
        if (response.getStatusCode() == HttpStatus.OK)
            emailService.sendRegConfirmationEmail(userCreateDto);
        return response;
    }

    @PostMapping("/forgot/password")
    public void sendEmailToRecoverPassword(@RequestBody RestorePasswordRequestDto requestDto) {
        if (!authService.checkIfUserExistsByEmail(requestDto.getEmail()))
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label + requestDto.getEmail());
        emailService.sendRestorePasswordEmail(requestDto.getEmail());
    }


    @PostMapping("/change/pass")
    public ResponseEntity<Object> recoverPassword(@RequestBody @Validated(New.class) JwtAuthRequest request) {
        log.info(LogMessage.TRY_CHANGE_PASSWORD.label);
        return ResponseEntity.of(
                Optional.of(userMapper
                        .userToUserResponseDto(userDetailsChangeService
                                .changePass(request))));
    }

    @PreAuthorize("hasAuthority('buyer:write') || hasAuthority('seller:write')")
    @PostMapping("/user/change/pass")
    public ResponseEntity<Object> changePassword(@RequestBody @Validated(New.class) JwtAuthRequest request) {
        log.info(LogMessage.TRY_CHANGE_PASSWORD.label);
        return ResponseEntity.of(
                Optional.of(userMapper
                        .userToUserResponseDto(userDetailsChangeService
                                .changePass(request))));
    }
}
