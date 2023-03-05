package tn.esprit.springfever.Services.Implementation;

import com.google.zxing.BarcodeFormat;
//import com.itextpdf.text.pdf.qrcode.BitMatrix;
//import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.EncodeHintType;
//import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;
//import com.itextpdf.text.pdf.qrcode.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.Services.Interfaces.TeamsMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceTeams;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

import com.google.zxing.WriterException;

//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.List;
import java.util.Map;

@Service
@Slf4j

public class ServiceTeamsImpl implements IServiceTeams{
    @Autowired
    UserRepository userRepository;
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TeamsRepository teamsRepository ;
    @Autowired
    ImageDataRepository imageDataRepository ;
    @Autowired
    TeamsMapper teamsMapper;

    @Autowired
    EventRepository eventRepository ;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public Teams addTeams(Teams teams) {
        log.info("teams was successfully added !");
        return teamsRepository.save(teams);
    }

    public String AssignImageToTeams(Long idTeam , Long id ){
        Teams teams =teamsRepository.findById(idTeam).orElse(null);
        ImageData image=imageDataRepository.findById(id).orElse(null);
        if(teams!=null && image!=null){
            teams.setImage(image);
            teamsRepository.save(teams);
            return "Image Is successffully affected To Teams ! ";
        }
        return "Teams Or Image Does not Exist ";
    }

    public String AssignProjectToTeams(Long idTeam , Long idProject ){
        Teams teams =teamsRepository.findById(idTeam).orElse(null);
        Project project=projectRepository.findById(idProject).orElse(null);
        if(teams!=null && project!=null){
            teams.setProject(project);
            teamsRepository.save(teams);
            return "project Is successffully affected To Teams ! ";
        }
        return "Teams Or project Does not Exist ";
    }


    /*
    @Override
    public Teams addTeams(Teams teams) throws IOException {

        // Load the tokenizer model
        InputStream modelIn = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);

        // Create a new tokenizer
        Tokenizer tokenizer = new TokenizerME(model);

        // Analyze the keywords from the student's diploma
        String diploma = "Diploma in Computer Science with a specialization in Web Development";
        String[] tokens = tokenizer.tokenize(diploma);

        // Display the separated keywords
        for (String token : tokens)
            log.info(token);
        //log takone
        log.info("teams was successfully added !");
        return teamsRepository.save(teams);
    }
*/
    @Override
    public List<Teams> getAllTeams() {return teamsRepository.findAll();}


    @Override
    public boolean deleteTeams(Long idTeam) {
        Teams existingTeams = teamsRepository.findById(idTeam).orElse(null);
        if(existingTeams!=null) {
            teamsRepository.delete(existingTeams);
            log.info("teams deleted");
            return true ;
        }
        log.info(" this team is not existing");
        return false;
    }


    @Override
    public Teams updateTeams(Long idTeam, TeamsDTO teamsDto) {
        Teams teams = teamsRepository.findById(idTeam).orElse(null);
        if (teams != null) {
            teamsMapper.updateTeamsFromDto(teamsDto, teams);
            teamsRepository.save(teams);
            log.info("teams was successfully updated !");
        }
        log.info("teams not found !");
        return  teams;

    }
/*
    @Override
    public void assignUserToTeams(Long id, Long idTeam) {
        Teams  T1 = teamsRepository.findByIdTeam(idTeam);
        User U1 = userRepository.findById(id).orElse(null);
        System.out.println("teams : "+T1);
        System.out.println("User : "+U1);
        System.out.println("teams: "+T1);
        U1.setTeams(T1);
        userRepository.save(U1);
    }
 */

    @Override
    public void assignUserToTeams() {
        List<User> users = userRepository.findAll();

        Event event = new Event();

        int numUsers = users.size();
        int numTeams = (int) Math.ceil((double) numUsers / 5);

        for (int i = 0; i < numTeams; i++) {
            Teams team = new Teams();
            team.setNameTeam("Team " + (i+1)) ;
            team.setQRcertificat("Certificate " + (i+1)) ;
            team.setNiveauEtude("niveau d'étude " + (i+1));
            teamsRepository.save(team);

            for (int j = i*5; j < Math.min((i+1)*5, numUsers); j++) {
                User user = users.get(j);
                user.setTeams(team);
                userRepository.save(user);

                String subject = "You have been assigned to a team for APP0 event";
                String message = "Dear " + user.getUsername()+ ",\n\n"
                        + "We are pleased to inform you that you have been assigned to " + team.getNameTeam()+ " for the APP0" +  ", which will be taking place on 14-03-2023 " +  " at ESPRIT" +  ".\n"
                        + "As a member of " + team.getNameTeam()  + ", you will be expected to attend all scheduled meetings and participate in team activities leading up to the event. Your contribution is crucial to our success, and we believe that your skills and experience will be a valuable asset to the team.\n\n"
                        + "Please be advised that you must arrive on time for the event .\n\n"
                        + "Thank you for your dedication and commitment to the success of this event. We look forward to working with you.\n\n"
                        + "Best regards,\n"
                        + "APP0 Team";


                sendEmail(user.getEmail(), subject, message);

            }
        }
    }

    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }




    @Override
    public List<Teams> getTeamsByProjectId(Long idProject) {
        return teamsRepository.findbyIdProject(idProject);
    }

    @Override
    public Teams getTeamsWithMaxProjectNote() {
        return null;
    }



/*
    @Override
    public String generateQr(Teams teams) {
        String qrCodeData = teams.toString();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);
        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = new BitMatrix(8, 8);
        try {
            bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (com.google.zxing.WriterException e) {
            // Handle writer exception
            e.printStackTrace();

        }// Save QR code as a PNG file
        String filePath = System.getProperty("teams.dir") + "/assets/" + "user-" + teams.getIdTeam() + "-" + teams.getNameTeam() + ".png"; // Specify the file path and name
        File qrCodeFile = new File(filePath);
        try {
            MatrixToImageWriter.writeToFile(bitMatrix, "png", qrCodeFile);
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();

        }

        // Return a view that shows the generated QR code
        ModelAndView modelAndView = new ModelAndView("qr-code");
        modelAndView.addObject("qrCodeFile", qrCodeFile);
        return filePath;
    }

*/


@Override
    public Optional<Teams> getTeamById(Long id) {
        return teamsRepository.findById(id);
    }
@Override
    public Teams saveTeam(Teams team) {
        return teamsRepository.save(team);
    }
@Override
    public byte[] generateQRCode(String teamName, String event) throws WriterException, IOException {
        int width = 350;
        int height = 350;
        String qrCodeText = teamName + ": " + event;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height, getQRCodeHints());
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();
        outputStream.close();
        return qrCodeBytes;
    }

    private static Map<EncodeHintType, ?> getQRCodeHints() {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.QR_VERSION, 20);
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        return hints;



    }


}
