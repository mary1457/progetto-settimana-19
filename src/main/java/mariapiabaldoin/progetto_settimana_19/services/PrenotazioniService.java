package mariapiabaldoin.progetto_settimana_19.services;


import mariapiabaldoin.progetto_settimana_19.entities.Evento;
import mariapiabaldoin.progetto_settimana_19.entities.Prenotazione;
import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.NotFoundException;
import mariapiabaldoin.progetto_settimana_19.exceptions.ValidationException;
import mariapiabaldoin.progetto_settimana_19.payloads.NewPrenotazioneDTO;
import mariapiabaldoin.progetto_settimana_19.repositories.EventiRepository;
import mariapiabaldoin.progetto_settimana_19.repositories.PrenotazioniRepository;
import mariapiabaldoin.progetto_settimana_19.repositories.UtentiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class PrenotazioniService {

    @Autowired
    private UtentiRepository utentiRepository;

    @Autowired
    private PrenotazioniRepository prenotazioniRepository;

    @Autowired
    private EventiRepository eventiRepository;


    public Prenotazione save(UUID utenteId, NewPrenotazioneDTO body) {


        Utente utente = utentiRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException(utenteId));
        Evento evento = eventiRepository.findById(body.eventoId())
                .orElseThrow(() -> new NotFoundException(body.eventoId()));

        if (controlloDisponibilita(evento) == false) {
            throw new ValidationException("I posti per questo evento non sono disponibili");
        }

        Prenotazione newPrenotazione = new Prenotazione(utente, evento);
        return this.prenotazioniRepository.save(newPrenotazione);


    }


    public boolean controlloDisponibilita(Evento evento) {
        List<Prenotazione> prenotazioni = filterDisponibilita(evento.getId());
        if (prenotazioni.size() == evento.getPostiDisponibili()) {
            return false;
        } else {
            return true;
        }

    }

    public List<Prenotazione> filterDisponibilita(UUID id) {
        return prenotazioniRepository.filterByEventoId(id);
    }

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return this.prenotazioniRepository.findAll(pageable);
    }

    public Prenotazione findById(long prenotazioneId) {
        return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }


    public void findByIdAndDelete(long prenotazioneId) {
        Prenotazione found = this.findById(prenotazioneId);
        this.prenotazioniRepository.delete(found);
    }


}
