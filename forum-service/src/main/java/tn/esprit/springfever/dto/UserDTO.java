package tn.esprit.springfever.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private List<RoleDTO> roles = new ArrayList<>();
    private List<String> interests;

    public UserDTO() {

    }
    public UserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
