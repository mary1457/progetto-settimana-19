package mariapiabaldoin.progetto_settimana_19.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Utente implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    public Utente(String nome, String cognome, String email, String password, boolean isOrganizzatore) {

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        if (isOrganizzatore == true) {
            this.ruolo = Ruolo.ORGANIZZATORE_DI_EVENTI;
        } else {
            this.ruolo = Ruolo.UTENTE_NORMALE;
        }
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }


    public String getUsername() {
        return this.getEmail();
    }
}
