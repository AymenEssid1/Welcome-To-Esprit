package tn.esprit.springfever.service;

import tn.esprit.springfever.domain.RDV;
import tn.esprit.springfever.domain.User;
import tn.esprit.springfever.model.RDVDTO;
import tn.esprit.springfever.repos.RDVRepository;
import tn.esprit.springfever.repos.UserRepository;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RDVService {

    private  RDVRepository rDVRepository;
    private  UserRepository userRepository;

    public RDVService( RDVRepository rDVRepository,  UserRepository userRepository) {
        this.rDVRepository = rDVRepository;
        this.userRepository = userRepository;
    }

    public List<RDVDTO> findAll() {
         List<RDV> rDVs = rDVRepository.findAll(Sort.by("idRDV"));
        return rDVs.stream()
                .map((rDV) -> mapToDTO(rDV, new RDVDTO()))
                .collect(Collectors.toList());
    }

    public RDVDTO get( Long idRDV) {
        return rDVRepository.findById(idRDV)
                .map(rDV -> mapToDTO(rDV, new RDVDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create( RDVDTO rDVDTO) {
         RDV rDV = new RDV();
        mapToEntity(rDVDTO, rDV);
        return rDVRepository.save(rDV).getIdRDV();
    }

    public void update( Long idRDV,  RDVDTO rDVDTO) {
         RDV rDV = rDVRepository.findById(idRDV)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rDVDTO, rDV);
        rDVRepository.save(rDV);
    }

    public void delete( Long idRDV) {
        rDVRepository.deleteById(idRDV);
    }

    private RDVDTO mapToDTO( RDV rDV,  RDVDTO rDVDTO) {
        rDVDTO.setIdRDV(rDV.getIdRDV());
        rDVDTO.setSalle(rDV.getSalle());
        rDVDTO.setDate(rDV.getDate());
        rDVDTO.setRDVuser(rDV.getRDVuser() == null ? null : rDV.getRDVuser().getUserID());
        return rDVDTO;
    }

    private RDV mapToEntity( RDVDTO rDVDTO,  RDV rDV) {
        rDV.setSalle(rDVDTO.getSalle());
        rDV.setDate(rDVDTO.getDate());
         User rDVuser = rDVDTO.getRDVuser() == null ? null : userRepository.findById(rDVDTO.getRDVuser())
                .orElseThrow(() -> new NotFoundException("rDVuser not found"));
        rDV.setRDVuser(rDVuser);
        return rDV;
    }

}
