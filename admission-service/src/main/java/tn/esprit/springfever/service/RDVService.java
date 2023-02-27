package tn.esprit.springfever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.springfever.config.MailConfiguration;
import tn.esprit.springfever.domain.DemandeAdmission;
import tn.esprit.springfever.domain.RDV;
import tn.esprit.springfever.domain.Salle;
import tn.esprit.springfever.domain.User;
import tn.esprit.springfever.model.RDVDTO;
import tn.esprit.springfever.model.SalleDTO;
import tn.esprit.springfever.repos.RDVRepository;
import tn.esprit.springfever.repos.SalleRepository;
import tn.esprit.springfever.repos.UserRepository;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RDVService {


    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SalleService salleService;
    @Autowired
    private MailConfiguration mailConfiguration;

    @Autowired
    private  RDVRepository rDVRepository;
    @Autowired
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

    public Long create( RDVDTO rDVDTO,Long idUser) throws MessagingException {
        User user= userRepository.findById(idUser).orElse(new User());
         RDV rDV = new RDV();
        mapToEntity(rDVDTO, rDV);
        rDV.setRDVuser(user);
        String emailBody = "TEST ";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("TEST");
        message.setText(emailBody);
        message.setTo(user.getDemandeAdmission().getMailParent());
       // mailConfiguration.sendEmail(message);
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
        rDVDTO.setDate(rDV.getDate());
        rDVDTO.setRDVuser(rDV.getRDVuser() == null ? null : rDV.getRDVuser().getUserID());
        return rDVDTO;
    }

    private RDV mapToEntity( RDVDTO rDVDTO,  RDV rDV) {
        rDV.setDate(rDVDTO.getDate());
         User rDVuser = rDVDTO.getRDVuser() == null ? null : userRepository.findById(rDVDTO.getRDVuser())
                .orElseThrow(() -> new NotFoundException("rDVuser not found"));
        rDV.setRDVuser(rDVuser);
        return rDV;
    }

    @Scheduled(cron = "*/10 * * * * *" )
    private void etatTuteur(){
        LocalDate l = LocalDate.now();
        List<User> users = userRepository.findByetatuserAndRDVuserRDVsDate();

    for(User user:users ){
        System.err.print("test");
        user.setEtatuser("disponible");
    }

    }



}
