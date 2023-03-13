
package tn.esprit.springfever.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String etatUser;
    private String password;

    private String image;
    private List<String> roles = new ArrayList<>();

    public UserDTO() {

    }
    @JsonCreator
    public UserDTO(@JsonProperty("id")Long id, @JsonProperty("username") String username, @JsonProperty("email")String email, @JsonProperty("image")String image, @JsonProperty("roles")List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.image = image;
        this.roles = roles;
    }
}
