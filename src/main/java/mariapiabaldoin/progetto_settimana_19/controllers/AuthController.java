package mariapiabaldoin.progetto_settimana_19.controllers;


import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.BadRequestException;
import mariapiabaldoin.progetto_settimana_19.payloads.NewUtenteDTO;
import mariapiabaldoin.progetto_settimana_19.payloads.UtenteLoginDTO;
import mariapiabaldoin.progetto_settimana_19.payloads.UtenteLoginResponseDTO;
import mariapiabaldoin.progetto_settimana_19.services.AuthService;
import mariapiabaldoin.progetto_settimana_19.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UtentiService utentiService;

    @PostMapping("/login")
    public UtenteLoginResponseDTO login(@RequestBody UtenteLoginDTO body) {
        return new UtenteLoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente save(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.utentiService.save(body);
    }
}