package ru.yandex.workshop.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.workshop.main.dto.validation.New;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.security.config.JwtTokenProvider;
import ru.yandex.workshop.security.dto.JwtRequest;
import ru.yandex.workshop.security.dto.UserDto;
import ru.yandex.workshop.security.exception.WrongDataDbException;
import ru.yandex.workshop.security.exception.WrongRegException;
import ru.yandex.workshop.security.mapper.UserMapper;
import ru.yandex.workshop.security.message.ExceptionMessage;
import ru.yandex.workshop.security.message.LogMessage;
import ru.yandex.workshop.security.model.Role;
import ru.yandex.workshop.security.model.User;
import ru.yandex.workshop.security.repository.UserRepository;
import ru.yandex.workshop.security.service.AuthService;
import ru.yandex.workshop.security.service.UserDetailsChangeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.ValidationException;
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


    @PostMapping("/auth/login")
    public ResponseEntity<Object> authorization(@RequestBody @Valid JwtRequest request) {
        log.info(LogMessage.TRY_AUTHORIZATION.label);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            Map<Object, Object> response = new HashMap<>();
            User user = repository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
            String token = jwtTokenProvider.generateToken(request.getEmail(), user.getRole().name());
            response.put("email", user.getEmail());
            response.put("token", token);

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
    public ResponseEntity<Object> createNewUser(@RequestBody @Valid UserDto userDto) throws ValidationException, WrongDataDbException {
        log.info(LogMessage.TRY_REGISTRATION.label);

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new WrongRegException(ExceptionMessage.CONFIRMED_PASSWORD_EXCEPTION.label);
        }

        if ((userDto.getRole().equals(Role.SELLER) || userDto.getRole().equals(Role.BUYER)) && (userDto.getPhone() == null || userDto.getPhone().isEmpty()))
            throw new ValidationException("Необходимо указать номер телефона. Телефонный номер должен начинаться с +7, затем - 10 цифр.");

        return ResponseEntity.of(Optional.of(userMapper.userToUserResponseDto(authService.createNewUser(userDto))));
    }

    @PostMapping("/change/pass")
    public ResponseEntity<Object> changePassword(@RequestBody @Validated(New.class) JwtRequest request) {
        log.info(LogMessage.TRY_CHANGE_PASSWORD.label);
        return ResponseEntity.of(Optional.of(userMapper.userToUserResponseDto(userDetailsChangeService.changePass(request))));
    }
}
