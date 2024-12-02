package fr.diginamic.hello.restController;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.mapper.VilleMapper;
import fr.diginamic.hello.services.VilleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VilleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VilleService villeService;

    @MockitoBean
    private VilleMapper villeMapper;

    @Test
    public void testAjouterVille_Succes() throws Exception {

        Ville ville = new Ville(13321, "Montpellier", null, 50000, new Departement(1, "34", "Hérault"));

        Mockito.when(villeMapper.toEntity(any(VilleDto.class))).thenReturn(ville);
        Mockito.when(villeService.extractVilleParNom(Mockito.eq("Montpellier"))).thenReturn(null);

        mockMvc.perform(post("/villes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomVille": "Montpellier",
                                  "nbHabitants": 50000,
                                  "codeDepartement": "34",
                                  "nomDepartement": "Hérault"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Ville ajoutée avec succès."));

        Mockito.verify(villeMapper).toEntity(any(VilleDto.class));
        Mockito.verify(villeService).insertVille(ville);
    }

// [!!!] LE TEST DE VALIDATION DANS LA CLASSE "VilleService" AU NIVEAU DE LA METHODE DE VALIDATION (A LA FIN) [!!!]

    @Test
    public void testAjouterVille_VilleExiste() throws Exception {

        Ville ville = new Ville(13321, "Montpellier", null, 50000, new Departement(1, "34", "Hérault"));


        Mockito.when(villeMapper.toEntity(Mockito.any(VilleDto.class))).thenReturn(ville);
        Mockito.when(villeService.extractVilleParNom("Montpellier")).thenReturn(ville);

        mockMvc.perform(post("/villes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nomVille": "Montpellier",
                                  "nbHabitants": 50000,
                                  "codeDepartement": "34",
                                  "nomDepartement": "Hérault"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("* FunctionalException : La ville existe déjà."));

        Mockito.verify(villeMapper).toEntity(any(VilleDto.class));
        Mockito.verify(villeService, never()).insertVille(any(Ville.class));
    }
}

