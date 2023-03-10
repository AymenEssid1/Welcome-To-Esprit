package tn.esprit.springfever.service;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.domain.DemandeAdmission;
import tn.esprit.springfever.domain.Option;
import tn.esprit.springfever.domain.Specialite;
import tn.esprit.springfever.model.SpecialiteDTO;
import tn.esprit.springfever.repos.DemandeAdmissionRepository;
import tn.esprit.springfever.repos.OptionRepository;
import tn.esprit.springfever.repos.SpecialiteRepository;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SpecialiteService {

    @Autowired
    private  SpecialiteRepository specialiteRepository;
    @Autowired
    private  DemandeAdmissionRepository demandeAdmissionRepository;
    @Autowired
    private  OptionRepository optionRepository;

    public SpecialiteService( SpecialiteRepository specialiteRepository,
             DemandeAdmissionRepository demandeAdmissionRepository,
             OptionRepository optionRepository) {
        this.specialiteRepository = specialiteRepository;
        this.demandeAdmissionRepository = demandeAdmissionRepository;
        this.optionRepository = optionRepository;
    }

    public List<SpecialiteDTO> findAll() {
         List<Specialite> specialites = specialiteRepository.findAll(Sort.by("idSpecialite"));
        return specialites.stream()
                .map((specialite) -> mapToDTO(specialite, new SpecialiteDTO()))
                .collect(Collectors.toList());
    }

    public SpecialiteDTO get( Long idSpecialite) {
        return specialiteRepository.findById(idSpecialite)
                .map(specialite -> mapToDTO(specialite, new SpecialiteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create( SpecialiteDTO specialiteDTO, Long IdAdmission,Long idoption) {
        DemandeAdmission demandeAdmission= demandeAdmissionRepository.findById(IdAdmission).orElse(new DemandeAdmission());
        Option o = optionRepository.findById(idoption).orElse(new Option());
         Specialite specialite = new Specialite();
        mapToEntity(specialiteDTO, specialite);
       // specialite.getDemandeAdmissions().add(demandeAdmission);
        specialite.setSpecialiteOption(o);
        return specialiteRepository.save(specialite).getIdSpecialite();
    }

    public void update( Long idSpecialite,  SpecialiteDTO specialiteDTO) {
         Specialite specialite = specialiteRepository.findById(idSpecialite)
                .orElseThrow(NotFoundException::new);
        mapToEntity(specialiteDTO, specialite);
        specialiteRepository.save(specialite);
    }

    public void delete( Long idSpecialite) {
        specialiteRepository.deleteById(idSpecialite);
    }

    private SpecialiteDTO mapToDTO( Specialite specialite,  SpecialiteDTO specialiteDTO) {
        specialiteDTO.setIdSpecialite(specialite.getIdSpecialite());
        specialiteDTO.setNomSpecialite(specialite.getNomSpecialite());

        specialiteDTO.setSpecialiteOption(specialite.getSpecialiteOption() == null ? null : specialite.getSpecialiteOption().getIdOption());
        return specialiteDTO;
    }

    private Specialite mapToEntity( SpecialiteDTO specialiteDTO,  Specialite specialite) {
        specialite.setNomSpecialite(specialiteDTO.getNomSpecialite());

         Option specialiteOption = specialiteDTO.getSpecialiteOption() == null ? null : optionRepository.findById(specialiteDTO.getSpecialiteOption())
                .orElseThrow(() -> new NotFoundException("specialiteOption not found"));
        specialite.setSpecialiteOption(specialiteOption);
        return specialite;
    }

}
