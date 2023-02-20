package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Repositories.BadgeRepo;
import tn.esprit.springfever.Repositories.FileSystemRepository;
import tn.esprit.springfever.Repositories.RoleRepo;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.Services.Interface.IFileLocationService;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.tools.ResourceNotFoundException;

import javax.validation.Valid;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
    IServiceUser iServiceUser;
    @Autowired
    IFileLocationService iFileLocationService;
    @Autowired
    BadgeRepo badgeRepository;

    @GetMapping("/getallusers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getby/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value="/ADD_USER",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<User> test(@RequestBody MultipartFile image, @RequestParam String user,@RequestParam RoleType roleType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(user,UserDTO.class);
        User u = new User();
        u.setFirstname(userDTO.getFirstname());
        u.setCin(userDTO.getCin());
        u.setLastname(userDTO.getLastname());
        u.setDob(userDTO.getDob());
        u.setPassword(userDTO.getPassword());
        u.setUsername(userDTO.getUsername());
        if(image!=null){
            System.out.println(image.getOriginalFilename());
            Image newImage = iFileLocationService.save(image);
            u.setImage(newImage);
        }
        iServiceUser.addUserAndAssignRole(u,roleType);

        return ResponseEntity.status(HttpStatus.CREATED).body(u);
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



    @PutMapping(value = "/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestBody MultipartFile image, @RequestParam String user, @RequestParam RoleType roleType, @PathVariable long id) throws Exception {
        User oguser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + id));

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO = objectMapper.readValue(user,UserDTO.class);

        oguser.setFirstname(userDTO.getFirstname());
        oguser.setCin(userDTO.getCin());
        oguser.setLastname(userDTO.getLastname());
        oguser.setDob(userDTO.getDob());
        oguser.setPassword(userDTO.getPassword());
        oguser.setUsername(userDTO.getUsername());
        System.out.println("aaaaaaaaaaggghhhh!!!!");

        if(image!=null){
            System.out.println(image.getOriginalFilename());
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




}
