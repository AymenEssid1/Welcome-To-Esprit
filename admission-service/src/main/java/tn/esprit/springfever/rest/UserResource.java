package tn.esprit.springfever.rest;

import tn.esprit.springfever.model.UserDTO;
import tn.esprit.springfever.service.UserService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private  UserService userService;

    public UserResource( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userID}")
    public ResponseEntity<UserDTO> getUser(@PathVariable  Long userID) {
        return ResponseEntity.ok(userService.get(userID));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid  UserDTO userDTO) {
        return new ResponseEntity<>(userService.create(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{userID}")
    public ResponseEntity<Void> updateUser(@PathVariable  Long userID,
            @RequestBody @Valid  UserDTO userDTO) {
        userService.update(userID, userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable  Long userID) {
        userService.delete(userID);
        return ResponseEntity.noContent().build();
    }

}
