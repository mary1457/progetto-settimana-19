package mariapiabaldoin.progetto_settimana_19.repositories;


import mariapiabaldoin.progetto_settimana_19.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, Long> {

    @Query("SELECT p FROM Prenotazione p WHERE p.evento.id = :id")
    List<Prenotazione> filterByEventoId(UUID id);

}
