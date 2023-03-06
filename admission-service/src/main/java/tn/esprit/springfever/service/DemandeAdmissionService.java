package tn.esprit.springfever.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.domain.*;
import tn.esprit.springfever.model.DemandeAdmissionDTO;
import tn.esprit.springfever.model.Diplome;
import tn.esprit.springfever.model.Niveau;
import tn.esprit.springfever.model.NomSpecialite;
import tn.esprit.springfever.repos.*;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class DemandeAdmissionService {

    @Autowired
    private  DemandeAdmissionRepository demandeAdmissionRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private RDVRepository rdvRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SpecialiteRepository specialiteRepository;

    // Configurez les informations d'identification de Twilio
    final String ACCOUNT_SID = "votre_SID_Twilio";
    final String AUTH_TOKEN = "votre_token_Twilio";

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

    public Long create(DemandeAdmission demandeAdmission, Long IdUser)  {
        User user = userRepository.findById(IdUser).orElse(new User());
        demandeAdmission.setCondidat(user);
        DemandeAdmission d = demandeAdmissionRepository.save(demandeAdmission);
        user.setDemandeAdmissionStudent(d);
        userRepository.save(user);
        RDV rdv= new RDV();
        d.setRdvDemande(rdv);
        rdv.setDemande(d);


        rdv.setDate(LocalDate.now().plusDays(7));
        rdvRepository.save(rdv);
        Salledispo(rdv.getIdRDV());
        Tuteurdispo(rdv.getIdRDV());

      /* Twilio.init("ACc2294319aa2eaba8e91273055538a50e", "3d8aaf138dd120e037c4c12ae42a6ccf");
        Message.creator(new PhoneNumber("+21655105372"),
                new PhoneNumber("+18654137235"),
                "Hello from Twilio ").create();*/
        return demandeAdmission.getIdAdmission();


    }
    public void affecterSalle(Long idrdv, Long idSalle) {
        RDV rdv = rdvRepository.findById(idrdv)
                .orElseThrow(() -> new NotFoundException("Demande d'admission non trouvée avec l'ID : " + idrdv));

        Salle salle = new Salle();
        salle.setIdsalle(idSalle);
        rdv.setSalle(salle);

        rdvRepository.save(rdv);
    }
    public void affecterTuteur(Long demandeid, Long idUser ) {
        DemandeAdmission demandeAdmission = demandeAdmissionRepository.findById(demandeid)
                .orElseThrow(() -> new NotFoundException("Demande d'admission non trouvée avec l'ID : " + demandeid));

       User Evaluateur= userRepository.findById(idUser).get();
        Evaluateur.getDemandeAdmissionsEvaluateur().add(demandeAdmission);
        demandeAdmission.setEvaluateur(Evaluateur);

        demandeAdmissionRepository.save(demandeAdmission) ;
        userRepository.save(Evaluateur) ;


     }
    public void Salledispo(Long idRdv) {
        // Récupération de toutes les salles existantes
        List<Salle> salles = salleRepository.findByEtat("disponible");
        int nbSalles = salles.size();


        // Affectation aléatoire d'une salle à la demande
        if (nbSalles > 0) {
            int indexSalle = new Random().nextInt(nbSalles);
            Salle salle = salles.get(indexSalle);
            affecterSalle(idRdv, salle.getIdsalle());
            salle.setEtat("non disponible");

            salleRepository.save(salle);

        }
    }
    public void Tuteurdispo(Long demandeid){

        // demande
        DemandeAdmission demandeAdmission = demandeAdmissionRepository.findById(demandeid).get();


        // Récupération de toutes les users existantes
        List<User> users = userRepository.findByetatuser("disponible");
        int nbUsers = users.size();

        System.out.println("liste des tuteurs dispo " + users.size());


        // Affectation aléatoire d'un user à la demande
        if (nbUsers > 0) {
            int indexUser = new Random().nextInt(nbUsers);
            System.out.println("index :" + indexUser);
            User evaluator = users.get(indexUser);
            System.out.println("******** evaluator of the user dispo" + evaluator.getUserID() + evaluator.getEtatuser());
            evaluator.getDemandeAdmissionsEvaluateur().add(demandeAdmission);
            demandeAdmission.setEvaluateur(evaluator);
            System.out.println("tutor who would be affected " + evaluator.getUserID());
            evaluator.setEtatuser("non disponible");
            System.out.println(evaluator.getUserID() + evaluator.getEtatuser());
            demandeAdmission.setEvaluateur(evaluator);
            demandeAdmissionRepository.save(demandeAdmission);
            userRepository.save(evaluator);

        }

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
        demandeAdmissionDTO.setTypeDemande(demandeAdmission.getTypeDemande());
        demandeAdmissionDTO.setDiplome(demandeAdmission.getDiplome());
        demandeAdmissionDTO.setNiveau(demandeAdmission.getNiveau());
        demandeAdmissionDTO.setCursus(demandeAdmission.getCursus());
        demandeAdmissionDTO.setCIN(demandeAdmission.getCIN());
        demandeAdmissionDTO.setNomParent(demandeAdmission.getNomParent());
        demandeAdmissionDTO.setPrenomParent(demandeAdmission.getPrenomParent());
        demandeAdmissionDTO.setMailParent(demandeAdmission.getMailParent());
        demandeAdmissionDTO.setTelParent(demandeAdmission.getTelParent());
        return demandeAdmissionDTO;
    }

    private DemandeAdmission mapToEntity( DemandeAdmissionDTO demandeAdmissionDTO,
             DemandeAdmission demandeAdmission) {
        demandeAdmission.setDateAdmission(demandeAdmissionDTO.getDateAdmission());
        demandeAdmission.setTypeDemande(demandeAdmissionDTO.getTypeDemande());
        demandeAdmission.setDiplome(demandeAdmissionDTO.getDiplome());
        demandeAdmission.setNiveau(demandeAdmissionDTO.getNiveau());
        demandeAdmission.setCursus(demandeAdmissionDTO.getCursus());
        demandeAdmission.setNomParent(demandeAdmissionDTO.getNomParent());
        demandeAdmission.setCIN(demandeAdmissionDTO.getCIN());
        demandeAdmission.setPrenomParent(demandeAdmissionDTO.getPrenomParent());
        demandeAdmission.setMailParent(demandeAdmissionDTO.getMailParent());
        demandeAdmission.setTelParent(demandeAdmissionDTO.getTelParent());


        User condidat = demandeAdmissionDTO.getCondidat() == null ? null : userRepository.findById(demandeAdmissionDTO.getCondidat().getUserID())
                .orElseThrow(() -> new NotFoundException("condidat not found"));
        demandeAdmission.setCondidat(condidat);


        User evaluateur = demandeAdmissionDTO.getEvaluateeur() == null ? null : userRepository.findById(demandeAdmissionDTO.getEvaluateeur().getUserID())
                .orElseThrow(() -> new NotFoundException("evaluateur not found"));
        demandeAdmission.setEvaluateur(evaluateur);


        RDV rdv = demandeAdmissionDTO.getRdvDemande() == null ? null : rdvRepository.findById(demandeAdmissionDTO.getRdvDemande().getIdRDV())
                .orElseThrow(() -> new NotFoundException("demandeUser not found"));
        demandeAdmission.setRdvDemande(rdv);
        return demandeAdmission;
    }



}
