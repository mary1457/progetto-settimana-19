package mariapiabaldoin.progetto_settimana_19.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record NewPrenotazioneDTO(

        @NotNull(message = "L'evento è obbligatorio!")
        UUID eventoId) {
}



