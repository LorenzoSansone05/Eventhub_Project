package it.academy.largesystems.eventhub.controller;

import it.academy.largesystems.eventhub.dto.AuthRequestDTO;
import it.academy.largesystems.eventhub.service.LoginService;
import it.academy.largesystems.eventhub.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;

    public AuthController(SignupService signupService,
                          LoginService loginService) {
        this.signupService = signupService;
        this.loginService = loginService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody AuthRequestDTO req) {
        signupService.signup(req.getEmail(), req.getPassword());
        return ResponseEntity.ok("Registrazione completata");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody AuthRequestDTO req) {
//        loginService.login(req.getEmail(), req.getPassword());
//        return ResponseEntity.ok("Login effettuato");
//    }

    // TEST LOGIN
    @GetMapping("/me")
    public String loginTest(Authentication auth) {
        return "Ciao " + auth.getName();
    }

}

