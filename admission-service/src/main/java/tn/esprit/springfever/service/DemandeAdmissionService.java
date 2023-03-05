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

    public Long create(DemandeAdmissionDTO demandeAdmissionDTO, Long IdUser)  {
        User user = userRepository.findById(IdUser).orElse(new User());
         DemandeAdmission demandeAdmission = new DemandeAdmission();
        mapToEntity(demandeAdmissionDTO, demandeAdmission);
        demandeAdmission.setUser(user);
        DemandeAdmission d = demandeAdmissionRepository.save(demandeAdmission);
        user.setDemandeAdmission(d);
        RDV rdv= new RDV();
        d.setRdvDemande(rdv);
        rdv.setDemandeRdv(d);


        rdv.setDate(LocalDate.now().plusDays(7));
        rdvRepository.save(rdv);
        Salledispo(rdv.getIdRDV());
        Tuteurdispo(rdv.getIdRDV());

        userRepository.save(user);
      /* Twilio.init("ACc2294319aa2eaba8e91273055538a50e", "3d8aaf138dd120e037c4c12ae42a6ccf");
        Message.creator(new PhoneNumber("+21655105372"),
                new PhoneNumber("+18654137235"),
                "Hello from Twilio ").create();*/
        return d.getIdAdmission();


    }
    public void affecterSalle(Long idrdv, Long idSalle) {
        RDV rdv = rdvRepository.findById(idrdv)
                .orElseThrow(() -> new NotFoundException("Demande d'admission non trouvée avec l'ID : " + idrdv));

        Salle salle = new Salle();
        salle.setIdsalle(idSalle);
        rdv.setRDVsalle(salle);

        rdvRepository.save(rdv);
    }
    public void affecterTuteur(Long idrdv, Long idUser) {
        RDV rdv = rdvRepository.findById(idrdv)
                .orElseThrow(() -> new NotFoundException("Demande d'admission non trouvée avec l'ID : " + idrdv));

        User user = new User();
        user.setUserID(idUser);
        rdv.setRDVuser(user);

        rdvRepository.save(rdv);
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
    public void Tuteurdispo(Long idRdv){
        // Récupération de toutes les users existantes
        List<User> users = userRepository.findByetatuser("disponible");
        int nbUsers = users.size();


        // Affectation aléatoire d'un user à la demande
        if (nbUsers > 0) {
            int indexUser = new Random().nextInt(nbUsers);
            User user = users.get(indexUser);
            affecterTuteur(idRdv, user.getUserID());
            user.setEtatuser("non disponible");
            userRepository.save(user);

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

    public void StatDemande() {

      int ING = 0;
        int PRE = 0;
       for (DemandeAdmission d : demandeAdmissionRepository.findAll()) {

            if (d.getDiplome().equals(Diplome.INGENIEURIE)) {
                ING++;
            }
            if (d.getDiplome().equals(Diplome.PREPA)) {
                PRE++;
            }
            if (ING > PRE) {
                System.out.print("on a plus  d'ingnieurs");
            }
            else if (PRE > ING) {
                System.out.print("on a plus  de prepas");

            } else {System.out.print("les diplomes sont egaux");}

        }
        int niv1=0;
        int niv2=0;
        int niv3=0;
        int niv4=0;
        int em=0;
        int gc=0;
        int it=0;
        int tc=0;

        List<DemandeAdmission> demandeAdmissions=demandeAdmissionRepository.findAll();
        List<Specialite> specialites=specialiteRepository.findAll();

        for (DemandeAdmission d:demandeAdmissions){
            if(d.getNiveau().equals(Niveau.UN)){
                niv1++;}
            if (d.getNiveau().equals(Niveau.DEUX)){
                niv2++;}
            if (d.getNiveau().equals(Niveau.TROIS)){
                niv3++;}
            if(d.getNiveau().equals(Niveau.QUATRE)){
                niv4++;}

        }
        if ((niv1>niv2)&&(niv1>niv3)&&(niv1>niv4)){
            System.out.print("le niveau 1 est supérieur");
        }
        if ((niv1==niv2)&&(niv1>niv3)&&(niv1>niv4)){
            System.out.print("le niveau 1 et 2 sont supérieur");
        }
        if ((niv1>niv2)&&(niv1==niv3)&&(niv1>niv4)){
            System.out.print("le niveau 1 et 3 sont supérieur");
        }
        if ((niv1>niv2)&&(niv1>niv3)&&(niv1==niv4)){
            System.out.print("les niveaux 1 et 4 sont supérieur");
        }
        if((niv2>niv3)&&(niv2>niv1)&&(niv2>niv4)){
            System.out.print("le niveau 2 est supérieur");
        }
        if((niv2==niv3)&&(niv2>niv1)&&(niv2>niv4)){
            System.out.print("les niveaux 2 et 3 sont supérieur");
        }
        if((niv2>niv3)&&(niv2>niv1)&&(niv2==niv4)){
            System.out.print("les niveaux 2 et 4 sont le plus");
        }
        if ((niv3>niv1)&&(niv3>niv2)&&(niv3>niv4)){
            System.out.print("les niveaux 3 sont le plus");
        }
        if ((niv3>niv1)&&(niv3>niv2)&&(niv3==niv4)){
            System.out.print("le niveau 3 et 4 est le plus");
        }
        if((niv4>niv1)&&(niv4>niv2)&&(niv4>niv3)){
            System.out.print("le niveau 4 est le plus");
        }
        else{
            System.out.print("tous les niveau sont egaux");

        }
        for(Specialite s:specialites){
            if(s.getNomSpecialite().equals(NomSpecialite.EM)){em++;}
            if(s.getNomSpecialite().equals(NomSpecialite.GC)){gc++;}
            if(s.getNomSpecialite().equals(NomSpecialite.IT)){it++;}
            if(s.getNomSpecialite().equals(NomSpecialite.TC)){tc++;}
        }

        if ((em>gc)&&(em>it)&&(em>tc)){
            System.out.print("le em est le plus");
        }
        if((gc>em)&&(gc>it)&&(gc>tc)){
            System.out.print("le gc est le plus");
        }
        if ((it>em)&&(it>gc)&&(it>tc)){
            System.out.print("le it est le plus");
        }
        if((tc>em)&&(tc>gc)&&(tc>it)){
            System.out.print("le tc est le plus");
        }
        else{
            System.out.print("tous les Specialiter sont egaux");

        }




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
        demandeAdmissionDTO.setUser(demandeAdmission.getUser() == null ? null : demandeAdmission.getUser().getUserID());
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
         User demandeUser = demandeAdmissionDTO.getUser() == null ? null : userRepository.findById(demandeAdmissionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("demandeUser not found"));
        demandeAdmission.setUser(demandeUser);
        RDV rdv = demandeAdmissionDTO.getRdvDemande() == null ? null : rdvRepository.findById(demandeAdmissionDTO.getRdvDemande())
                .orElseThrow(() -> new NotFoundException("demandeUser not found"));
        demandeAdmission.setRdvDemande(rdv);
        return demandeAdmission;
    }



}
