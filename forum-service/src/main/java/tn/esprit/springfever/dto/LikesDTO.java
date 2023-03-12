package tn.esprit.springfever.dto;

import lombok.Data;
import tn.esprit.springfever.entities.Reaction;
import java.time.LocalDateTime;

@Data
public class LikesDTO {
    private Long likeId;
    private Reaction type;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
