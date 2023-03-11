package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.Repositories.*;
import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Security.services.UserDetailsImpl;
import tn.esprit.springfever.Services.Interface.IFileLocationService;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.configuration.MailConfiguration;
import tn.esprit.springfever.configuration.UsernameValidator;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.payload.Request.LoginRequest;
import tn.esprit.springfever.payload.Response.JwtResponse;


import javax.validation.Valid;
import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth" )

public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepository;
    @Autowired
    RoleRepo roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    IServiceUser iServiceUser;
    @Autowired
    IFileLocationService iFileLocationService;
    @Autowired
    BadgeRepo badgeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailConfiguration mailConfiguration;
    @Autowired
    private UsernameValidator usernameValidator;


    @PostMapping("/signinV2")
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> authenticateUserV2(@Valid @RequestBody LoginRequest loginRequest) throws IOException, GeoIp2Exception {

            User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
            String timeLeft=iServiceUser.checkBan(user);
            if(timeLeft!="a")
            {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is temporarily locked. Please try again in " + timeLeft + ".");}


            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = jwtUtils.generateJwtToken(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());

                // Reset the failed login attempt count if authentication succeeds
                if (user != null) {
                    user.setFailedLoginAttempts(0);
                    userRepository.save(user);
                }

                return ResponseEntity.ok(new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
            } catch (AuthenticationException e) {
                // Authentication failed, increment failed login attempt count
                if (user != null) {
                    int failedAttempts = user.getFailedLoginAttempts() + 1;
                    user.setFailedLoginAttempts(failedAttempts);
                    userRepository.save(user);

                    // Check if the user should be banned
                    if (failedAttempts >= 3) {

                        iServiceUser.timeoutuser(user);
                        // User is banned, return error response
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is temporarily locked. Please try again later.");
                    }
                }}

                // Authentication failed, return error response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");}

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String resetPasswordRequest) {

        User user = userRepository.findByUsername(resetPasswordRequest)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String newPassword = generateRandomPassword();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        String emailBody = "Hello " + user.getFirstname() + ",\n\n" +
                "Your password has been reset. Your new password is: " + newPassword + "\n\n" +
                "Please change your password after logging in.\n\n" +
                "Regards,\nThe Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("PASSWORD RESET");
        message.setText(emailBody);
        message.setTo(user.getEmail());
        mailConfiguration.sendEmail(message);



        return ResponseEntity.ok("Password reset successfully. Please check your email.");
    }

    private String generateRandomPassword() {
        String password = RandomStringUtils.randomAlphanumeric(8);
        return password;
    }









    @PostMapping(value="/signUpV3",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
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
                                           @RequestParam RoleType roleType
            ) throws Exception {

        if(!usernameValidator.isValid(username)){
            JSONObject message = new JSONObject();
            message.put("message", "Invalid Username!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.toString());
        }

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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*  @PostMapping(value="/signUpV2",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<User> test(@RequestBody MultipartFile image, @RequestParam String user, @RequestParam RoleType roleType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(user,UserDTO.class);
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

        if (roleType.name().equals("STUDENT")){u.setPayment_status(0);} else{u.setPayment_status(-1);}

        if(image!=null){
            System.out.println(image.getOriginalFilename());
            Image newImage = iFileLocationService.save(image);
            u.setImage(newImage);
        }
        iServiceUser.addUserAndAssignRole(u,roleType);

        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }*/

    //////////////////////////////////////////////////////////////////////////////////////////////////////


    /*

    @PostMapping("/signin")
     public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {





        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //jwtUtils.setJwtSecret("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
          String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }*/




    /*
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRolename(RoleType.CANDIDATE);

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRolename(RoleType.SUPER_ADMIN)
                                ;
                        roles.add(adminRole);

                        break;
                    case "teacher":
                        Role modRole = roleRepository.findByRolename(RoleType.TEACHER)
                                ;
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRolename(RoleType.CANDIDATE)
                               ;
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

*/





}