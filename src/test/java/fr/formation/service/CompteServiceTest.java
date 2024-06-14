package fr.formation.service;

import fr.formation.model.Compte;
import fr.formation.model.Utilisateur;
import fr.formation.repo.CompteRepository;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.CreateCompteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompteServiceTest {

    @Mock
    private CompteRepository compteRepository;
    

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private CompteService compteService;

    private Compte compte;

    @BeforeEach
    void setUp() {
        compte = new Compte();
        compte.setId(1);
        compte.setPlatformname("Test Platform");
        compte.setDescription("Test Description");
        compte.setUserName("Test User");
        compte.setEmail("test@example.com");
        compte.setAdressUrl("http://example.com");
        compte.setPassword("password123");
        compte.setDateAdded(LocalDateTime.now());
        compte.setDateUpdate(LocalDateTime.now());
    }

    @Test
    void shouldGetAllComptes() {
        // given
        Mockito.when(compteRepository.findAll()).thenReturn(List.of(compte, compte, compte));

        // when
        List<Compte> comptes = compteService.getAllComptes();

        // then
        assertEquals(3, comptes.size());
        Mockito.verify(compteRepository).findAll();
    }

    @Test
    void shouldGetCompteById() {
        // given
        Mockito.when(compteRepository.findById(1)).thenReturn(Optional.of(compte));

        // when
        Compte foundCompte = compteService.getCompteById(1);

        // then
        assertEquals(1, foundCompte.getId());
        Mockito.verify(compteRepository).findById(1);
    }

    @Test
    void shouldAddCompte() {
        // given
        CreateCompteRequest request = new CreateCompteRequest();
        request.setUtilisateurId(1);
        request.setPlatformname("Test Platform");
        request.setDescription("Test Description");
        request.setUserName("Test User");
        request.setEmail("test@example.com");
        request.setAdressUrl("http://example.com");
        request.setPassword("password123");

        // Mocking the behavior of utilisateurRepository and compteRepository
        Utilisateur utilisateur = new Utilisateur(); // Replace with appropriate code for your Utilisateur object
        when(utilisateurRepository.getById(anyInt())).thenReturn(utilisateur);

        // Mock the behavior of compteRepository.save()
        when(compteRepository.save(any())).thenAnswer(invocation -> {
            Compte savedCompte = invocation.getArgument(0);
            savedCompte.setId(1); // Simulate setting the ID after save
            return savedCompte;
        });

        // when
        Integer newCompteId = compteService.addCompte(request);

        // then
        assertEquals(1, newCompteId); // Ensure that the returned ID matches what is expected
    }



    @Test
    void shouldFindByUtilisateurId() {
        // given
        Mockito.when(compteRepository.findByUtilisateurId(1)).thenReturn(List.of(compte, compte));

        // when
        List<Compte> comptes = compteService.findByUtilisateurId(1);

        // then
        assertEquals(2, comptes.size());
        Mockito.verify(compteRepository).findByUtilisateurId(1);
    }
}
