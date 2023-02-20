package tn.esprit.springfever.service;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.domain.DemandeAdmission;
import tn.esprit.springfever.domain.User;
import tn.esprit.springfever.model.DemandeAdmissionDTO;
import tn.esprit.springfever.repos.DemandeAdmissionRepository;
import tn.esprit.springfever.repos.UserRepository;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DemandeAdmissionService {

    @Autowired
    private  DemandeAdmissionRepository demandeAdmissionRepository;
    @Autowired
    private  UserRepository userRepository;

    public DemandeAdmissionService( DemandeAdmissionRepository demandeAdmissionRepository,
             UserRepository userRepository) {
        this.demandeAdmissionRepository = demandeAdmissionRepository;
        this.userRepository = userRepository;
    }

    public List<DemandeAdmissionDTO> findAll() {
         List<DemandeAdmission> demandeAdmissions = demandeAdmissionRepository.findAll(Sort.by("idAdmission"));
        return demandeAdmissions.stream()
                .map((demandeAdmission) -> mapToDTO(demandeAdmission, new DemandeAdmissionDTO()))
                .collect(Collectors.toList());
    }

    public DemandeAdmissionDTO get(Long idAdmission) {
        return demandeAdmissionRepository.findById(idAdmission)
                .map(demandeAdmission -> mapToDTO(demandeAdmission, new DemandeAdmissionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(DemandeAdmissionDTO demandeAdmissionDTO, Long IdUser)  {
        User user = userRepository.findById(IdUser).orElse(new User());
         DemandeAdmission demandeAdmission = new DemandeAdmission();
        mapToEntity(demandeAdmissionDTO, demandeAdmission);
        demandeAdmission.setDemandeUser(user);
        return demandeAdmissionRepository.save(demandeAdmission).getIdAdmission();

    }

    public void update(Long idAdmission,DemandeAdmissionDTO demandeAdmissionDTO) {
         DemandeAdmission demandeAdmission = demandeAdmissionRepository.findById(idAdmission)
                .orElseThrow(NotFoundException::new);
        mapToEntity(demandeAdmissionDTO, demandeAdmission);
        demandeAdmissionRepository.save(demandeAdmission);
    }

    public void delete( Long idAdmission) {

        demandeAdmissionRepository.deleteById(idAdmission);
    }

    private DemandeAdmissionDTO mapToDTO( DemandeAdmission demandeAdmission,
             DemandeAdmissionDTO demandeAdmissionDTO) {
        demandeAdmissionDTO.setIdAdmission(demandeAdmission.getIdAdmission());
        demandeAdmissionDTO.setDateAdmission(demandeAdmission.getDateAdmission());
        demandeAdmissionDTO.setStatus(demandeAdmission.getStatus());
        demandeAdmissionDTO.setDiplome(demandeAdmission.getDiplome());
        demandeAdmissionDTO.setNiveau(demandeAdmission.getNiveau());
        demandeAdmissionDTO.setCursus(demandeAdmission.getCursus());
        demandeAdmissionDTO.setSpecialite(demandeAdmission.getSpecialite());
        demandeAdmissionDTO.setOption(demandeAdmission.getOption());
        demandeAdmissionDTO.setFrais(demandeAdmission.getFrais());
        demandeAdmissionDTO.setNomParent(demandeAdmission.getNomParent());
        demandeAdmissionDTO.setPrenomParent(demandeAdmission.getPrenomParent());
        demandeAdmissionDTO.setMailParent(demandeAdmission.getMailParent());
        demandeAdmissionDTO.setTelParent(demandeAdmission.getTelParent());
        demandeAdmissionDTO.setDemandeUser(demandeAdmission.getDemandeUser() == null ? null : demandeAdmission.getDemandeUser().getUserID());
        return demandeAdmissionDTO;
    }

    private DemandeAdmission mapToEntity( DemandeAdmissionDTO demandeAdmissionDTO,
             DemandeAdmission demandeAdmission) {
        demandeAdmission.setDateAdmission(demandeAdmissionDTO.getDateAdmission());
        demandeAdmission.setStatus(demandeAdmissionDTO.getStatus());
        demandeAdmission.setDiplome(demandeAdmissionDTO.getDiplome());
        demandeAdmission.setNiveau(demandeAdmissionDTO.getNiveau());
        demandeAdmission.setCursus(demandeAdmissionDTO.getCursus());
        demandeAdmission.setSpecialite(demandeAdmissionDTO.getSpecialite());
        demandeAdmission.setOption(demandeAdmissionDTO.getOption());
        demandeAdmission.setFrais(demandeAdmissionDTO.getFrais());
        demandeAdmission.setNomParent(demandeAdmissionDTO.getNomParent());
        demandeAdmission.setPrenomParent(demandeAdmissionDTO.getPrenomParent());
        demandeAdmission.setMailParent(demandeAdmissionDTO.getMailParent());
        demandeAdmission.setTelParent(demandeAdmissionDTO.getTelParent());
         User demandeUser = demandeAdmissionDTO.getDemandeUser() == null ? null : userRepository.findById(demandeAdmissionDTO.getDemandeUser())
                .orElseThrow(() -> new NotFoundException("demandeUser not found"));
        demandeAdmission.setDemandeUser(demandeUser);
        return demandeAdmission;
    }

}
