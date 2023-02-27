package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Media")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Media implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "media_id")
    private Long mediaId;
    private String name;
    private String location;
    @Lob
    @JsonIgnore
    byte[] content;
    private String type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime createdAt;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime updatedAt;

    public Media(String name, String location, byte[] content, String type) {
        this.name = name;
        this.location = location;
        this.content = content;
        this.type=type;
    }
}
