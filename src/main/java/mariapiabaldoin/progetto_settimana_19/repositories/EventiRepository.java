package mariapiabaldoin.progetto_settimana_19.repositories;


import mariapiabaldoin.progetto_settimana_19.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EventiRepository extends JpaRepository<Evento, UUID> {


}
