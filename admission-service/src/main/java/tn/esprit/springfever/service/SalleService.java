package tn.esprit.springfever.service;


import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.domain.Salle;
import tn.esprit.springfever.model.SalleDTO;
import tn.esprit.springfever.repos.SalleRepository;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class SalleService {
    @Autowired
    private  SalleRepository salleRepository;

    public SalleService( SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    public List<SalleDTO> findAll() {
         List<Salle> salles = salleRepository.findAll(Sort.by("idsalle"));
        return salles.stream()
                .map((salle) -> mapToDTO(salle, new SalleDTO()))
                .collect(Collectors.toList());
    }

    public SalleDTO get( Long id) {
        return salleRepository.findById(id)
                .map(salle -> mapToDTO(salle, new SalleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Salle create( int numSalle ) {
         Salle salle = new Salle();
         salle.setEtat("disponible");
         salle.setNumSalle(numSalle);
        return salleRepository.save(salle);
    }

    public void update( Long id,  SalleDTO salleDTO) {
         Salle salle = salleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(salleDTO, salle);
        salleRepository.save(salle);
    }

    public void delete( Long id) {
        salleRepository.deleteById(id);
    }

    private SalleDTO mapToDTO( Salle salle,  SalleDTO salleDTO) {
        salleDTO.setId(salle.getIdsalle());
        salleDTO.setNumSalle(salle.getNumSalle());
        return salleDTO;
    }

    private Salle mapToEntity( SalleDTO salleDTO,  Salle salle) {
        salle.setNumSalle(salleDTO.getNumSalle());
        salle.setEtat(salleDTO.getEtat());
        return salle;
    }

}
