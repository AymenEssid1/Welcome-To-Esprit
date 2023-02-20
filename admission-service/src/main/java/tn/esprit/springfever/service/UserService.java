package tn.esprit.springfever.service;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.domain.User;
import tn.esprit.springfever.model.UserDTO;
import tn.esprit.springfever.repos.UserRepository;
import tn.esprit.springfever.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired

    private  UserRepository userRepository;

    public UserService( UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
         List<User> users = userRepository.findAll(Sort.by("userID"));
        return users.stream()
                .map((user) -> mapToDTO(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    public UserDTO get( Long userID) {
        return userRepository.findById(userID)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create( UserDTO userDTO) {
         User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserID();
    }

    public void update( Long userID,  UserDTO userDTO) {
         User user = userRepository.findById(userID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete( Long userID) {
        userRepository.deleteById(userID);
    }

    private UserDTO mapToDTO( User user,  UserDTO userDTO) {
        userDTO.setUserID(user.getUserID());
        return userDTO;
    }

    private User mapToEntity( UserDTO userDTO,  User user) {
        return user;
    }

}
