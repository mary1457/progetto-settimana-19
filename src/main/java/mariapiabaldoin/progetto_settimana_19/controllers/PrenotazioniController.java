package mariapiabaldoin.progetto_settimana_19.controllers;


import mariapiabaldoin.progetto_settimana_19.entities.Prenotazione;
import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.BadRequestException;
import mariapiabaldoin.progetto_settimana_19.payloads.NewPrenotazioneDTO;
import mariapiabaldoin.progetto_settimana_19.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {
    @Autowired
    private PrenotazioniService prenotazioniService;


    @GetMapping
    @PreAuthorize("hasAuthority('ORGANIZZATORE_DI_EVENTI')")
    public Page<Prenotazione> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {

        return this.prenotazioniService.findAll(page, size, sortBy);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione save(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody @Validated NewPrenotazioneDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.prenotazioniService.save(currentAuthenticatedUtente.getId(), body);
    }


}
