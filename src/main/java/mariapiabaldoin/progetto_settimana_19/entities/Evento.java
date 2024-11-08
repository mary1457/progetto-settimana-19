package mariapiabaldoin.progetto_settimana_19.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Evento {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private String luogo;
    private int postiDisponibili;
    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    public Evento(String titolo, String descrizione, LocalDate data, String luogo, int postiDisponibili, Utente utente) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.luogo = luogo;
        this.postiDisponibili = postiDisponibili;
        this.utente = utente;
    }
}
