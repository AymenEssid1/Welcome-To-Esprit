package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Repositories.BadgeRepo;
import tn.esprit.springfever.Repositories.FileSystemRepository;
import tn.esprit.springfever.Repositories.RoleRepo;
import tn.esprit.springfever.Repositories.UserRepo;

import tn.esprit.springfever.configuration.SMS_service;
import tn.esprit.springfever.Services.Interface.IFileLocationService;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.tools.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BadgeRepo badgerepo;
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    IServiceUser iServiceUser;
    @Autowired
    IFileLocationService iFileLocationService;
    @Autowired
    BadgeRepo badgeRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    SMS_service sms_service;


    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping("/getallusers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getby/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        System.out.println(user.toString());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value="/ADD_USER",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> signUpV3(@RequestBody MultipartFile image,
                                           @RequestParam String username,
                                           @RequestParam String firstname,
                                           @RequestParam String lastname,
                                           @RequestParam String email,
                                           @RequestParam String phoneNumber,
                                           @RequestParam String cin,
                                           @RequestParam String dob,
                                           @RequestParam String password,
                                           @RequestParam RoleType roleType) throws Exception {
        String user="{\"username\": \""+username+"\",   \"email\": \""+email+"\",   \"firstname\": \""+firstname+"\",   \"lastname\": \""+lastname+"\",   \"cin\": "+cin+",   \"phoneNumber\": \""+phoneNumber+"\",   \"dob\": \""+dob+"\",   \"password\": \""+password+"\" }";

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(user, UserDTO.class);

        // Validate input attributes
        if (userDTO.getFirstname() == null || userDTO.getFirstname().matches(".*\\d.*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Firstname");
        }
        if (userDTO.getLastname() == null || userDTO.getLastname().matches(".*\\d.*")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Lastname");
        }


        if (userDTO.getPhoneNumber() == null || !userDTO.getPhoneNumber().matches("\\d{8}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Phone Number");
        }
        if (userDTO.getEmail() == null || !userDTO.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email Address");
        }

        // Create User object
        User u = new User();
        u.setFirstname(userDTO.getFirstname());
        u.setCin(userDTO.getCin());
        u.setLastname(userDTO.getLastname());
        u.setDob(userDTO.getDob());
        u.setEmail(userDTO.getEmail());
        u.setPassword(encoder.encode(userDTO.getPassword()));
        u.setUsername(userDTO.getUsername());
        LocalDateTime currentDateTime = LocalDateTime.now();
        u.setCreationDate(currentDateTime);
        u.setPhoneNumber(userDTO.getPhoneNumber());

        if (roleType.name().equals("STUDENT")) {
            u.setPayment_status(0);
        } else {
            u.setPayment_status(-1);
        }

        if(image != null){
            System.out.println(image.getOriginalFilename());
            Image newImage = iFileLocationService.save(image);
            u.setImage(newImage);
        }

        // Add user and assign role
        iServiceUser.addUserAndAssignRole(u,roleType);

        return ResponseEntity.status(HttpStatus.CREATED).body(u.toString());
    }




    @PutMapping(value = "/UPDATE_USER/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestBody MultipartFile image, @PathVariable long id , @RequestParam(required = false) String username,
                                           @RequestParam(required = false) String firstname,
                                           @RequestParam(required = false) String lastname,
                                           @RequestParam(required = false) String email,
                                           @RequestParam(required = false) String phoneNumber,
                                           @RequestParam(required = false) String dob,
                                           @RequestParam(required = false) String password, @RequestParam RoleType roleType) throws Exception {


        String user="{\"username\": \""+username+"\",   \"email\": \""+email+"\",   \"firstname\": \""+firstname+"\",   \"lastname\": \""+lastname+"\",   \"phoneNumber\": \""+phoneNumber+"\",   \"dob\": \""+dob+"\",   \"password\": \""+password+"\" }";

        User oguser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

        System.out.println(oguser);

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(user, UserDTO.class);

        if(firstname != null) {
            oguser.setFirstname(userDTO.getFirstname());
        }

        if(lastname != null) {
            oguser.setLastname(userDTO.getLastname());
        }

        if(dob != null) {
            oguser.setDob(userDTO.getDob());
        }

        if(password != null) {
            oguser.setPassword(encoder.encode(userDTO.getPassword()));
        }

        if(username != null) {
            oguser.setUsername(userDTO.getUsername());
        }

        if(phoneNumber != null) {
            oguser.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if(email != null) {
            oguser.setEmail(userDTO.getEmail());
        }

        System.out.println("aaaaaaaaaaggghhhh!!!!");

        if(image!=null){
           // System.out.println(image.getOriginalFilename());
            Image newImage = iFileLocationService.save(image);
            oguser.setImage(newImage);
        }
        iServiceUser.addUserAndAssignRole(oguser,roleType);

        return ResponseEntity.ok(oguser);
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    @PostMapping(value = "/users/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadUsersFile(@RequestParam("file") MultipartFile file) throws IOException {
        List<User> users = iServiceUser.readUsersFromExcelFile(file.getInputStream());
        iServiceUser.saveAll(users);
        return ResponseEntity.ok("Users uploaded successfully.");
    }


    @GetMapping(value = "/badge/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> downloadImage(@PathVariable Long imageId) {
        try {
            Badge badge = badgeRepository.findById(imageId).orElse(null);
            FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(badge.getQrCode());
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////
    private Map<String, String> getRequestHeaders() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));

        }
        System.out.println(request.getHeader(HttpHeaders.AUTHORIZATION));
        return headers;
    }
}
