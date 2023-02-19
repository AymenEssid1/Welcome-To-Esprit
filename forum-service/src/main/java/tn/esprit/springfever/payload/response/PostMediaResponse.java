package tn.esprit.springfever.payload.response;

import lombok.*;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.enums.MediaType;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMediaResponse {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    private int content;
    private String location;




}

