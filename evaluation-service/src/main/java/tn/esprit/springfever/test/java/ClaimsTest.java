package tn.esprit.springfever.test.java ;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tn.esprit.springfever.EvaluationService;
import tn.esprit.springfever.Services.Implementation.ServiceClaimsImpl;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.repositories.ClaimRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EvaluationService.class)


public class ClaimsTest {

    @Autowired
    private ServiceClaimsImpl entityService;

    @MockBean
    private ClaimRepository entityRepository;

    @Test
    public void testCache() {
        Long entityId = 1L;
        Claim entity = new Claim(entityId, "Test claim");

        // Mock repository to return the entity
        Mockito.when(entityRepository.findById(entityId)).thenReturn(Optional.of(entity));

        // Call the method multiple times
        Claim result1 = entityService.findById(entityId);
        Claim result2 = entityService.findById(entityId);
        Claim result3 = entityService.findById(entityId);
        Claim result4 = entityService.findById(entityId);
        Claim result5 = entityService.findById(entityId);
        Claim result6 = entityService.findById(entityId);

        // Verify that the repository method was only called once
        Mockito.verify(entityRepository, Mockito.times(1)).findById(entityId);
        System.out.println("the repository method was only called once");
        // Verify that the results are the same and not null
        Assertions.assertNotNull(result1);
        Assertions.assertEquals(result1, result2);
        Assertions.assertEquals(result1, result3);
        System.out.println("the results are the same and not null");

    }


}
