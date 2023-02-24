package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.Repositories.*;
import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Security.services.UserDetailsImpl;
import tn.esprit.springfever.Services.Interface.IFileLocationService;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.configuration.MailConfiguration;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.payload.Request.LoginRequest;
import tn.esprit.springfever.payload.Request.SignupRequest;
import tn.esprit.springfever.payload.Response.JwtResponse;
import tn.esprit.springfever.payload.Response.MessageResponse;


import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private BanRepository banRepository;

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


    @GetMapping("hello")
    public String hello() {
        return "hello" ;
    }

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
    }


    @PostMapping("/signinV2")
    public ResponseEntity<?> authenticateUserV2(@Valid @RequestBody LoginRequest loginRequest) {


        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        if (user != null && user.getBan() != null && user.getBan().getExpiryTime() != null &&
                LocalDateTime.now().isBefore(user.getBan().getExpiryTime())) {
            // User is banned, return error response
            Duration remainingTime = Duration.between(LocalDateTime.now(), user.getBan().getExpiryTime());
            String timeLeft = String.format("%d minutes, %d seconds", remainingTime.toMinutes(), remainingTime.getSeconds() % 60);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is temporarily locked. Please try again in " + timeLeft + ".");}


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
                        Ban ban = new Ban();
                        ban.setLastFailedLoginAttempt(LocalDateTime.now());
                        ban.setExpiryTime(LocalDateTime.now().plusMinutes(100));
                        ban.setUser(user);
                        user.setBan(ban);
                        banRepository.save(ban);
                        userRepository.save(user);


                        // User is banned, return error response
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account is temporarily locked. Please try again later.");
                    }
                }}

                // Authentication failed, return error response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");}

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


    @PostMapping(value="/signUpV2",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
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
        if(image!=null){
            System.out.println(image.getOriginalFilename());
            Image newImage = iFileLocationService.save(image);
            u.setImage(newImage);
        }
        iServiceUser.addUserAndAssignRole(u,roleType);

        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }






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

















}