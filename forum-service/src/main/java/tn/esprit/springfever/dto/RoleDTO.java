package tn.esprit.springfever.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoleDTO {

    private Long id;

    private String name;
    public RoleDTO(String name){
        this.name = name;
        this.id = 0L;
    }

    @JsonCreator
    public RoleDTO(@JsonProperty("id") Long id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
