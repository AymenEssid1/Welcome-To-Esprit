package tn.esprit.springfever.dto;

import lombok.Data;

@Data
public class RoleDTO {

    private Long id;

    private String name;

    public RoleDTO(String name){
        this.name = name;
    }
}
