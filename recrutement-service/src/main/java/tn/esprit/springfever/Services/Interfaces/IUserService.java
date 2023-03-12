package tn.esprit.springfever.Services.Interfaces;



import com.fasterxml.jackson.core.JsonProcessingException;
import tn.esprit.springfever.DTO.UserDTO;
 import java.util.List;

public interface IUserService {
    public UserDTO getUserDetailsFromToken(String token) throws JsonProcessingException;
    public UserDTO getUserDetailsFromId(Long id) throws JsonProcessingException;

    public List<UserDTO> getUserDetailsFromIds(List<Long> list) throws JsonProcessingException;
}