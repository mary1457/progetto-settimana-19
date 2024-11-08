package mariapiabaldoin.progetto_settimana_19.services;


import mariapiabaldoin.progetto_settimana_19.entities.Utente;
import mariapiabaldoin.progetto_settimana_19.exceptions.UnauthorizedException;
import mariapiabaldoin.progetto_settimana_19.payloads.UtenteLoginDTO;
import mariapiabaldoin.progetto_settimana_19.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    private UtentiService utentiService;

    @Autowired
    private JWT jwt;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UtenteLoginDTO body) {

        Utente found = this.utentiService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), found.getPassword())) {

            String accessToken = jwt.createToken(found);

            return accessToken;
        } else {

            throw new UnauthorizedException("Credenziali errate!");
        }
    }

}
