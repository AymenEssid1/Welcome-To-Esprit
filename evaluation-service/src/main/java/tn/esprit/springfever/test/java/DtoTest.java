package tn.esprit.springfever.test.java;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.EvaluationService;
import tn.esprit.springfever.Services.Implementation.ServiceClaimsImpl;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.repositories.ClaimRepository;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EvaluationService.class)
public class DtoTest {

    @Autowired
    private ServiceClaimsImpl claimService;

    @MockBean
    private ClaimRepository claimRepository;

    @Test
     public void testUpdateClaim() {

       long entityId = 1L;
        Claim claim = new Claim();
        claim.setIdClaim(entityId);
        claim.setDecision("first decision");



        // Mock repository to return the entity
        Mockito.when(claimRepository.findById(entityId)).thenReturn(Optional.of(claim));

         try {
            claim = claimService.addClaim(claim, "ahmed.gouiaa@esprit.tn");
        } catch (IOException e) {
          System.out.print("la la la ");
        }
        //System.out.print(claim);

        // Create a new ClaimDTO with updated data
        ClaimDTO claimDTO = new ClaimDTO();
    claimDTO.setDecision("updated");
        System.out.println("claimDto"+claimDTO.toString());
        System.out.println("claim is null: " + (claim == null));

        // Map the ClaimDTO to the existing Claim entity
        ClaimMapper mapper = Mappers.getMapper(ClaimMapper.class);
         mapper.updateClaimFromDto(claimDTO, claim);
        // Update the Claim entity in the database                /*
        //         this mock behavior is telling the program to simulate the behavior of the claimRepository.save()
        //         method by returning a specified instance of Claim
        //         when it is called.
        //         This allows you to test the behavior of your code without actually saving anything to the database.
        //         */
      when(claimRepository.save(any(Claim.class))).thenReturn(claim);
          claimService.updateClaim(1L,claimDTO);
        // Fetch the Claim entity from the database
     when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        Claim updatedClaim = claimService.findById(1L);
        // Assert that the updated data in the Claim entity matches the data in the updated ClaimDTO
        //assertEquals(claimDTO.getClaimSubject(), updatedClaim.getClaimSubject());
      assertEquals(claimDTO.getDecision(), updatedClaim.getDecision());

    }


}
