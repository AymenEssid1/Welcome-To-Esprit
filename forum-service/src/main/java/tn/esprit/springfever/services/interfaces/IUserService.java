package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.esprit.springfever.dto.UserDTO;

public interface IUserService {
    public UserDTO getUserDetailsFromToken(String token) throws JsonProcessingException;
    public UserDTO getUserDetailsFromId(Long id) throws JsonProcessingException;
}
