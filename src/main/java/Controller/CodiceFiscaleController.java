package Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Codice Fiscale", description = "Calcolo anagrafica da codice fiscale (data di nascita ed età)")
public class CodiceFiscaleController {

    @GetMapping("/dati-/{codiceFiscale}")
    @Operation(
            summary = "Calcola la tua data di nascita ed la tua età ",
            description = "Tramite un codice fiscale, restituisce la data di nascita e l'età",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dati trovati"),
                    @ApiResponse(responseCode = "400", description = "Codice fiscale non valido"),
                    @ApiResponse(responseCode = "500", description = "Errore")
            }
    )
    public Map<String, Object> getDati(
            @PathVariable @Schema(description = "Codice fiscale italiano", example = "RSSMRA85M01H501Z") String codiceFiscale) {

        Map<String, Object> response = new HashMap<>();

        try {
            LocalDate dataNascita = estraiDataDiNascita(codiceFiscale);
            int eta = calcoloEta(dataNascita);

            response.put("dataNascita", dataNascita.toString());
            response.put("eta", eta);
        } catch (IllegalArgumentException e) {
            response.put("errore", e.getMessage());
        }

        return response;
    }

    private LocalDate estraiDataDiNascita(String codiceFiscale) {
        if (codiceFiscale == null || codiceFiscale.length() < 11) {
            throw new IllegalArgumentException("Codice fiscale non valido");
        }

        String anno = codiceFiscale.substring(6, 8);
        String mese = codiceFiscale.substring(8, 9);
        String giorno = codiceFiscale.substring(9, 11);

        int anno2 = Integer.parseInt(anno);
        int mese2 = "ABCDEHLMPRST".indexOf(mese.charAt(0)) + 1;
        int giorno2 = Integer.parseInt(giorno);

        if (giorno2 > 31) {
            giorno2 -= 40;
        }


        LocalDate odierna = LocalDate.now();
        int annoCorrente = odierna.getYear() % 100;
        int secolo = (anno2 <= annoCorrente) ? 2000 : 1900;
        LocalDate dataNascita = LocalDate.of(secolo + anno2, mese2, giorno2);

        return dataNascita;
    }

    private int calcoloEta(LocalDate dataNascita) {
        return Period.between(dataNascita, LocalDate.now()).getYears();
    }
}

