package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.enums.ClaimStatus;
import tn.esprit.springfever.enums.ClaimSubject;

import java.util.List;

@EnableJpaRepositories
public interface ClaimRepository extends JpaRepository<Claim,Long> {

    public List<Claim> findByUser(Long id ) ;
    @Query("SELECT C FROM Claim C  WHERE  C.claimStatus=:claimStatus and C.claimSubject=:claimSubject ")
    public List<Claim> findAllByClaimSubjectAndClaimStatus(ClaimSubject claimSubject, ClaimStatus claimStatus) ;

}
