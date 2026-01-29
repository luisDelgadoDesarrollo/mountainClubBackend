package luis.delgado.clubmontana.backend.api.controllers;

import jakarta.validation.Valid;
import luis.delgado.clubmontana.backend.api.dtos.LoginRequestDto;
import luis.delgado.clubmontana.backend.api.dtos.TokenResponseDto;
import luis.delgado.clubmontana.backend.api.mappers.AuthControllerMapper;
import luis.delgado.clubmontana.backend.domain.userCases.AuthUseCases;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthUseCases authUseCases;
  private final AuthControllerMapper userControllerMapper;

  public AuthController(AuthUseCases authUseCases, AuthControllerMapper userControllerMapper) {
    this.authUseCases = authUseCases;
    this.userControllerMapper = userControllerMapper;
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponseDto> login(
      @Valid @RequestBody LoginRequestDto loginRequestDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            userControllerMapper.tokenResponseToTokenResponseDto(
                authUseCases.login(
                    userControllerMapper.loginRequestDtoToLoginCommand(loginRequestDto))));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDto> refresh(@Valid @RequestBody String refreshToken) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            userControllerMapper.tokenResponseToTokenResponseDto(
                authUseCases.refreshToken(refreshToken)));
  }
}
