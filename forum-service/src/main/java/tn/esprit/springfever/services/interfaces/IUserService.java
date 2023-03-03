package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;

import java.util.List;

public interface IUserService {
    public UserDTO getUserDetailsFromToken(String token) throws JsonProcessingException;
    public UserDTO getUserDetailsFromId(Long id) throws JsonProcessingException;

    public List<UserDTO> getUserDetailsFromIds(List<Long> list) throws JsonProcessingException;
}
