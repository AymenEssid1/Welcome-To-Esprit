package tn.esprit.springfever.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private String msg;
    private UserDTO sender;
    private UserDTO receiver;
    private String convId;
    private LocalDateTime timestamps;

}
