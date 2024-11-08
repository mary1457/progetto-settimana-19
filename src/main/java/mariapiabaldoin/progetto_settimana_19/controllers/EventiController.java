package mariapiabaldoin.progetto_settimana_19.controllers;


import mariapiabaldoin.progetto_settimana_19.entities.Evento;
import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.BadRequestException;
import mariapiabaldoin.progetto_settimana_19.payloads.NewEventoDTO;
import mariapiabaldoin.progetto_settimana_19.services.EventiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/eventi")
public class EventiController {
    @Autowired
    private EventiService eventiService;


    @GetMapping
    public Page<Evento> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy) {

        return this.eventiService.findAll(page, size, sortBy);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZZATORE_DI_EVENTI')")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento save(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody @Validated NewEventoDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.eventiService.save(currentAuthenticatedUtente.getId(), body);
    }


    @GetMapping("/{eventoId}")
    public Evento findById(@PathVariable UUID eventoId) {
        return this.eventiService.findById(eventoId);
    }


    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE_DI_EVENTI')")
    public Evento findByIdAndUpdate(@PathVariable UUID eventoId, @RequestBody @Validated NewEventoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Ci sono stati errori nel payload!");
        }

        return this.eventiService.findByIdAndUpdate(eventoId, body);
    }


    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE_DI_EVENTI')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID eventoId) {
        this.eventiService.findByIdAndDelete(eventoId);
    }


}
