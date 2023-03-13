package tn.esprit.springfever.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.type.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.lookups.v1.*;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.domain.DemandeAdmission;
import tn.esprit.springfever.domain.Image;
import tn.esprit.springfever.model.*;
import tn.esprit.springfever.service.DemandeAdmissionService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.service.FileLocationService;
import tn.esprit.springfever.service.SalleService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;


@RestController
@RequestMapping(value = "/api/demandeAdmissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemandeAdmissionResource {

    @Autowired
    private SalleService salleService;
    @Autowired
    private  DemandeAdmissionService demandeAdmissionService;
    @Autowired
    private FileLocationService fileLocationService;

    public DemandeAdmissionResource( DemandeAdmissionService demandeAdmissionService) {
        this.demandeAdmissionService = demandeAdmissionService;
    }

    @GetMapping
    public ResponseEntity<List<DemandeAdmissionDTO>> getAllDemandeAdmissions() {
        return ResponseEntity.ok(demandeAdmissionService.findAll());

    }

    @GetMapping("/{idAdmission}")
    public ResponseEntity<DemandeAdmissionDTO> getDemandeAdmission(
            @PathVariable  Long idAdmission) {
        return ResponseEntity.ok(demandeAdmissionService.get(idAdmission));
    }

    @GetMapping("/StatDiplome")
    public Map<String,Long> statDiplome(){
        return demandeAdmissionService.statDiplome();
    }

    @PostMapping(value = "/add_iduser",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> signUpV3(@RequestBody MultipartFile CIN,
                                           @RequestParam Cursus cursus,
                                           @RequestParam Diplome diplome,
                                           @RequestParam String mailParent,
                                           @RequestParam Niveau niveau,
                                           @RequestParam String nomParent,
                                           @RequestParam String prenomParent,
                                           @RequestParam String telParent,
                                           @RequestParam TypeDemande typeDemande,
                                           @RequestParam Long userid,
                                           @RequestParam Long specialiterid) throws Exception {
        String user="{\"cursus\": \""+cursus+"\",      \"diplome\": \""+diplome+"\",   \"mailParent\": \""+mailParent+"\",   \"niveau\": \""+niveau+"\" ,   \"nomParent\": \""+nomParent+"\",   \"prenomParent\": \""+prenomParent+"\",   \"telParent\": \""+telParent+"\",  \"typeDemande\": \""+typeDemande+"\"  }";
        ObjectMapper objectMapper = new ObjectMapper();
        DemandeAdmissionDTO demandeAdmissionDTO = objectMapper.readValue(user, DemandeAdmissionDTO.class);
    DemandeAdmission d = new DemandeAdmission();
        d.setCursus(demandeAdmissionDTO.getCursus());
        LocalDate currentDateTime = LocalDate.now();
        d.setDateAdmission(currentDateTime);
        d.setDiplome(demandeAdmissionDTO.getDiplome());
        d.setMailParent(demandeAdmissionDTO.getMailParent());
        d.setNiveau(demandeAdmissionDTO.getNiveau());
        d.setNomParent(demandeAdmissionDTO.getNomParent());
        d.setPrenomParent(demandeAdmissionDTO.getPrenomParent());
        d.setTelParent(demandeAdmissionDTO.getTelParent());
        d.setTypeDemande(demandeAdmissionDTO.getTypeDemande());
        if(CIN != null){
            System.out.println(CIN.getOriginalFilename());
            Image newImage = fileLocationService.save(CIN);
            d.setCIN(newImage.getLocation());
        }
        demandeAdmissionService.create(d,userid,specialiterid);
        return ResponseEntity.status(HttpStatus.CREATED).body(d.toString());


    }
    /*
        public ResponseEntity<Long> createDemandeAdmission(
            @RequestBody @Valid DemandeAdmission demandeAdmission,
            @PathVariable Long iduser) throws MessagingException {
        Long idDemande = demandeAdmissionService.create(demandeAdmission, iduser);


        return new ResponseEntity<>(idDemande, HttpStatus.CREATED);
    }*/

    /*
    @PostMapping("/{iduser}")
    public ResponseEntity<Long> createDemandeAdmission(
            @RequestPart @Valid DemandeAdmission demandeAdmission,
            @RequestPart(name = "image") MultipartFile image,
            @PathVariable Long iduser) throws MessagingException, IOException {
        if (image != null && !image.isEmpty()) {
            String imagePath = DemandeAdmissionService.saveImage(image,demandeAdmission);
            demandeAdmission.setCIN(imagePath);
        }
        Long idDemande = demandeAdmissionService.create(demandeAdmission, iduser);


        return new ResponseEntity<>(idDemande, HttpStatus.CREATED);
    }
*/
    @PutMapping("/{idAdmission}")
    public ResponseEntity<Void> updateDemandeAdmission(@PathVariable  Long idAdmission,
            @RequestBody @Valid  DemandeAdmissionDTO demandeAdmissionDTO) throws JsonProcessingException {
        demandeAdmissionService.update(idAdmission, demandeAdmissionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idAdmission}")
    public ResponseEntity<Void> deleteDemandeAdmission(@PathVariable  Long idAdmission) {
        demandeAdmissionService.delete(idAdmission);
        return ResponseEntity.noContent().build();
    }



}
