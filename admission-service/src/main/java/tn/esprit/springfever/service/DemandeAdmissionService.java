package tn.esprit.springfever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.domain.*;
import tn.esprit.springfever.model.*;
import tn.esprit.springfever.repos.*;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DemandeAdmissionService {

    @Autowired
    private  DemandeAdmissionRepository demandeAdmissionRepository;
    @Autowired
    private RDVRepository rdvRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SpecialiteRepository specialiteRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;


    // Configurez les informations d'identification de Twilio
    final String ACCOUNT_SID = "votre_SID_Twilio";
    final String AUTH_TOKEN = "votre_token_Twilio";

    public DemandeAdmissionService( DemandeAdmissionRepository demandeAdmissionRepository) {
        this.demandeAdmissionRepository = demandeAdmissionRepository;
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

    public Long create(DemandeAdmission demandeAdmission, Long IdUser,Long idspecialiter) throws Exception {
        UserDTO user = userService.getUserDetailsFromId(IdUser);
        demandeAdmission.setCondidat(user.getId());

        DemandeAdmission d = demandeAdmissionRepository.save(demandeAdmission);
        Specialite s = specialiteRepository.findById(idspecialiter).orElse(new Specialite());
        if (s.getDemandeAdmissions() == null){
            List<DemandeAdmission> list = new ArrayList<>();
            list.add(demandeAdmission);
            s.setDemandeAdmissions(list);
        }else {
            s.getDemandeAdmissions().add(demandeAdmission);
        }
        specialiteRepository.save(s);
        RDV rdv= new RDV();
        d.setRdvDemande(rdv);
        rdv.setDemande(d);


        rdv.setDate(LocalDate.now().plusDays(7));
        rdvRepository.save(rdv);
        Salledispo(rdv.getIdRDV());
        Tuteurdispo(d.getIdAdmission());
        if(demandeAdmission.getTypeDemande().equals(TypeDemande.PRESENTIELLE)){
        //send E-mail au condidat
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title >Admission contest</title>\n" +
                "    <style>\n" +
                "      /* Styles pour le corps de l'e-mail */\n" +
                "      body {\n" +
                "        font-family: Arial, sans-serif;\n" +
                "        font-size: 16px;\n" +
                "        color: #333;\n" +
                "      }\n" +
                "      /* Styles pour les en-têtes */\n" +
                "      h1, h2, h3 {\n" +
                "        color: #555;\n" +
                "      }\n" +
                "      /* Styles pour les boutons */\n" +
                "      .button {\n" +
                "        display: inline-block;\n" +
                "        background-color: #008CBA;\n" +
                "        color: #fff;\n" +
                "        padding: 10px 20px;\n" +
                "        border-radius: 5px;\n" +
                "        text-decoration: none;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                    "    <h1>Notification of your Application For Admission</h1>\n" +
                "    <p> " + ",</p>\n" +
                "    <p></p>\n" +
                "    <p> Greetings,\n" +user.getUsername()+
                "<br>Thank you for your interest in ESPRIT.  </p>\n" +
                "    <ul>\n" +
                "      <li>Interview Date:"+ demandeAdmission.getDateAdmission().plusDays(7)+"</li>\n" +

                "    </ul>\n" +
                "    <ul>\n" +
                "      <li>Interview Classroom:"+ demandeAdmission.getRdvDemande().getSalle()+"</li>\n" +

                "    </ul>\n" +
                "    <p>If you have any questions or concerns, please don't hesitate to contact us.</p>\n" +
                "    <p>Thank you for your confidence.</p>\n" +
                "    <p>Cordially,</p>\n" +
                "    <h2>Spring Fever</h2>\n" +
                "    <p><a href=\"[lien vers votre site web]\" class=\"button\">Visit our Web-Site</a></p>\n" +
                "  </body>\n" +
                "</html>\n";
                sendEmail(user.getEmail(),"Admission Contest",html);}

    else {
        String meetingId = "ksv-wxsg-xwp";
        String password = "mypassword";
        String meetingLink = generateGoogleMeetLink(meetingId);
        String htmlgooglemeet="<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title >Admission contest</title>\n" +
                "    <style>\n" +
                "      /* Styles pour le corps de l'e-mail */\n" +
                "      body {\n" +
                "        font-family: Arial, sans-serif;\n" +
                "        font-size: 16px;\n" +
                "        color: #333;\n" +
                "      }\n" +
                "      /* Styles pour les en-têtes */\n" +
                "      h1, h2, h3 {\n" +
                "        color: #555;\n" +
                "      }\n" +
                "      /* Styles pour les boutons */\n" +
                "      .button {\n" +
                "        display: inline-block;\n" +
                "        background-color: #008CBA;\n" +
                "        color: #fff;\n" +
                "        padding: 10px 20px;\n" +
                "        border-radius: 5px;\n" +
                "        text-decoration: none;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>Notification of your Application For Admission</h1>\n" +
                "    <p> " + ",</p>\n" +
                "    <p></p>\n" +
                "    <p> Greetings,\n" +user.getUsername()+
                "<br>Thank you for your interest in ESPRIT.  </p>\n" +
                "    <ul>\n" +
                "      <li>Date de le entretien:"+ demandeAdmission.getDateAdmission().plusDays(7)+"</li>\n" +

                "    </ul>\n" +
                "    <p>If you have any questions or concerns, please don't hesitate to contact us.</p>\n" +
                "    <p>Thank you for your confidence.</p>\n" +
                "    <p>Cordially,</p>\n" +
                "    <h2>Spring Fever</h2>\n" +
                "    <p><a href="+generateGoogleMeetLink(meetingId)+" class=\"button\">Your Link Meet</a></p>\n" +
                "  </body>\n" +
                "</html>\n";
        sendEmail(user.getEmail(),"Admission Contest",htmlgooglemeet);
    }


      /* Twilio.init("ACc2294319aa2eaba8e91273055538a50e", "3d8aaf138dd120e037c4c12ae42a6ccf");
        Message.creator(new PhoneNumber("+21655105372"),
                new PhoneNumber("+18654137235"),
                "Your child's application for admission has been submited successfully  ").create();*/
        return demandeAdmission.getIdAdmission();



    }



    public void sendEmail(String to, String subject, String html) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }
    public void affecterSalle(Long idrdv, Long idSalle) {
        RDV rdv = rdvRepository.findById(idrdv)
                .orElseThrow(() -> new NotFoundException("Demande d'admission non trouvée avec l'ID : " + idrdv));

        Salle salle = new Salle();
        salle.setIdsalle(idSalle);
        rdv.setSalle(salle);

        rdvRepository.save(rdv);
    }

    public String generateGoogleMeetLink(String meetingId) {
        String baseUrl = "https://meet.google.com/";
        String meetingUrl = baseUrl + meetingId;
        return meetingUrl;
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
    public void Tuteurdispo(Long demandeid) throws JsonProcessingException {

        // demande
        DemandeAdmission demandeAdmission = demandeAdmissionRepository.findById(demandeid).get();


        // Récupération de toutes les users existantes
        List<UserDTO> users = userService.getAvailableTuttors("disponible");
        int nbUsers = users.size();



        // Affectation aléatoire d'un user à la demande
        if (nbUsers > 0) {
            int indexUser = new Random().nextInt(nbUsers);
            UserDTO evaluator = users.get(indexUser);
            userService.changeUser(evaluator.getId());
            demandeAdmission.setEvaluateur(evaluator.getId());
            demandeAdmissionRepository.save(demandeAdmission);

        }

    }


    public Map<String,Long> statDiplome(){
        Map<String,Long> result = new HashMap<>();
        result.put("PREPA",demandeAdmissionRepository.countByDiplome(Diplome.PREPA));
        result.put("INGENIEURIE",demandeAdmissionRepository.countByDiplome(Diplome.INGENIEURIE));
        return result;

    }



    public void update(Long idAdmission,DemandeAdmissionDTO demandeAdmissionDTO) throws JsonProcessingException {
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
             DemandeAdmission demandeAdmission) throws JsonProcessingException {
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


        Long condidat = demandeAdmissionDTO.getCondidat() == null ? null : userService.getUserDetailsFromId(demandeAdmission.getCondidat()).getId();

        demandeAdmission.setCondidat(condidat);

        Long evaluateur = demandeAdmissionDTO.getEvaluateeur() == null ? null : userService.getUserDetailsFromId(demandeAdmission.getEvaluateur()).getId();

        demandeAdmission.setEvaluateur(evaluateur);


        RDV rdv = demandeAdmissionDTO.getRdvDemande() == null ? null : rdvRepository.findById(demandeAdmissionDTO.getRdvDemande().getIdRDV())
                .orElseThrow(() -> new NotFoundException("demandeUser not found"));
        demandeAdmission.setRdvDemande(rdv);
        return demandeAdmission;
    }


    public static String saveImage(MultipartFile image, DemandeAdmission demandeAdmission) throws IOException {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        Path path = Paths.get("uploads");
        Files.createDirectories(path);
        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            demandeAdmission.setCIN(filePath.toString());
            return filePath.toString();
        } catch (IOException e) {
            throw new IOException("Could not save file " + fileName, e);
        }

    }



}
