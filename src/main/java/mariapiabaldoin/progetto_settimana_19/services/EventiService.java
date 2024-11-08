package mariapiabaldoin.progetto_settimana_19.services;


import mariapiabaldoin.progetto_settimana_19.entities.Evento;
import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.BadRequestException;
import mariapiabaldoin.progetto_settimana_19.exceptions.NotFoundException;
import mariapiabaldoin.progetto_settimana_19.payloads.NewEventoDTO;
import mariapiabaldoin.progetto_settimana_19.repositories.EventiRepository;
import mariapiabaldoin.progetto_settimana_19.repositories.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class EventiService {

    @Autowired
    private EventiRepository eventiRepository;

    @Autowired
    private UtentiRepository utentiRepository;

    public Evento save(UUID utenteId, NewEventoDTO body) {

        Utente utente = utentiRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException(utenteId));

        if (LocalDate.now().isBefore(body.data())) {
            Evento newEvento = new Evento(body.titolo(), body.descrizione(), body.data(), body.luogo(), body.postiDisponibili(), utente);
            return this.eventiRepository.save(newEvento);
        } else {
            throw new BadRequestException("La data " + body.data() + " non può essere antecedente ad oggi");
        }


    }

    public Page<Evento> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return this.eventiRepository.findAll(pageable);
    }

    public Evento findById(UUID eventoId) {
        return this.eventiRepository.findById(eventoId).orElseThrow(() -> new NotFoundException(eventoId));
    }

    public Evento findByIdAndUpdate(UUID eventoId, NewEventoDTO body) {

        Evento found = this.findById(eventoId);

        if (LocalDate.now().isBefore(body.data())) {
            found.setTitolo(body.titolo());
            found.setDescrizione(body.descrizione());
            found.setData(body.data());
            found.setLuogo(body.luogo());
            found.setPostiDisponibili(body.postiDisponibili());


        } else {
            throw new BadRequestException("La data " + body.data() + " non può essere antecedente ad oggi");
        }


        return this.eventiRepository.save(found);
    }


    public void findByIdAndDelete(UUID eventoId) {
        Evento found = this.findById(eventoId);
        this.eventiRepository.delete(found);
    }


}
