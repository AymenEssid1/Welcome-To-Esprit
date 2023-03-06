package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "PostView")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class PostViews  implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private Long user;
    @ManyToOne
    @JsonIgnore
    private Post post;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime timestamps;

    public PostViews(Long user, Post p, LocalDateTime timestamps){
        this.user = user;
        this.post = p;
        this.timestamps=timestamps;
    }
}
