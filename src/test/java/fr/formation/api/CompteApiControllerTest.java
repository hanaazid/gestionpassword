package fr.formation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.formation.model.Compte;
import fr.formation.request.CreateCompteRequest;
import fr.formation.request.ModifyCompteRequest;
import fr.formation.service.CompteService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CompteApiControllerTest {
    private final static String ENDPOINT = "/api/compte";

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Mock
    private CompteService compteService;

    @InjectMocks
    private CompteApiController ctrl;

    @BeforeEach
    public void beforeEach() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.ctrl).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    void shouldGetAllComptes() throws Exception {
        // given
        Mockito.when(compteService.getAllComptes()).thenReturn(List.of(new Compte(), new Compte(), new Compte()));

        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
        result.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)));

        Mockito.verify(compteService).getAllComptes();
    }

    @Test
    void shouldGetCompteById() throws Exception {
        // given
        Compte compte = new Compte();
        compte.setId(1);
        Mockito.when(compteService.getCompteById(1)).thenReturn(compte);

        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/1"));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        Mockito.verify(compteService).getCompteById(1);
    }

    @Test
    void shouldAddCompte() throws Exception {
        // given
        CreateCompteRequest request = new CreateCompteRequest();
        request.setUtilisateurId(1);
        request.setPlatformname("Test Platform");
        request.setDescription("Test Description");
        request.setUserName("Test User");
        request.setEmail("test@example.com");
        request.setAdressUrl("http://example.com");
        request.setPassword("password123");

        Integer newCompteId = 1;

        Mockito.when(compteService.addCompte(Mockito.any())).thenReturn(newCompteId);

        // when
        ResultActions result = this.mockMvc.perform(
            MockMvcRequestBuilders
                .post(ENDPOINT)
                .content(this.mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isCreated());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newCompteId));

        Mockito.verify(compteService).addCompte(Mockito.any());
    }

    @Test
    void shouldUpdateCompte() throws Exception {
        // given
        ModifyCompteRequest request = new ModifyCompteRequest();
        request.setPlatformname("Updated Platform");
        request.setDescription("Updated Description");
        request.setUserName("Updated User");
        request.setEmail("updated@example.com");
        request.setAdressUrl("http://updated.com");
        request.setPassword("newpassword123");

        Compte existingCompte = new Compte();
        existingCompte.setId(1);
        existingCompte.setPlatformname("Existing Platform");
        existingCompte.setDescription("Existing Description");
        existingCompte.setUserName("Existing User");
        existingCompte.setEmail("existing@example.com");
        existingCompte.setAdressUrl("http://existing.com");
        existingCompte.setPassword("existingpassword");
        existingCompte.setDateAdded(LocalDateTime.now());
        existingCompte.setDateUpdate(LocalDateTime.now());

        Mockito.when(compteService.existsCompteById(1)).thenReturn(true);
        Mockito.when(compteService.getCompteById(1)).thenReturn(existingCompte);
        Mockito.when(compteService.updateCompte(Mockito.any())).thenReturn(existingCompte);

        // when
        ResultActions result = this.mockMvc.perform(
            MockMvcRequestBuilders
                .put(ENDPOINT + "/1")
                .content(this.mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.platformname").value("Updated Platform"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"));

        Mockito.verify(compteService).updateCompte(Mockito.any());
    }

    @Test
    void shouldDeleteCompte() throws Exception {
        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT + "/1"));

        // then
        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(compteService).deleteCompte(1);
    }

    @Test
    void shouldGetComptesByUtilisateurId() throws Exception {
        // given
        Mockito.when(compteService.findByUtilisateurId(1)).thenReturn(List.of(new Compte(), new Compte()));

        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/utilisateurs/1"));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
        result.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

        Mockito.verify(compteService).findByUtilisateurId(1);
    }
}
