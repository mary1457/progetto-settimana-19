package mariapiabaldoin.progetto_settimana_19.services;


import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.BadRequestException;
import mariapiabaldoin.progetto_settimana_19.exceptions.NotFoundException;
import mariapiabaldoin.progetto_settimana_19.payloads.NewUtenteDTO;
import mariapiabaldoin.progetto_settimana_19.repositories.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtentiService {

    @Autowired
    private UtentiRepository utentiRepository;


    @Autowired
    private PasswordEncoder bcrypt;

    public Utente save(NewUtenteDTO body) {

        this.utentiRepository.findByEmail(body.email()).ifPresent(

                utente -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso!");
                }
        );


        Utente newUtente = new Utente(body.nome(), body.cognome(), body.email(), bcrypt.encode(body.password()), body.isOrganizzatore());


        return this.utentiRepository.save(newUtente);
    }

    public Page<Utente> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return this.utentiRepository.findAll(pageable);
    }

    public Utente findById(UUID utenteId) {
        return this.utentiRepository.findById(utenteId).orElseThrow(() -> new NotFoundException(utenteId));
    }


    public void findByIdAndDelete(UUID utenteId) {
        Utente found = this.findById(utenteId);
        this.utentiRepository.delete(found);
    }


    public Utente findByEmail(String email) {
        return this.utentiRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato"));
    }
}

