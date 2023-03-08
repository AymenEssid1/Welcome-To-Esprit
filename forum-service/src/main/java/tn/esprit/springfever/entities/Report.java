package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Report implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private Long user;
    @Column(name = "description")
    private String desc;
    @ManyToOne
    @Nullable
    @JsonIgnore
    private Comment comment;
    @ManyToOne
    @Nullable
    @JsonIgnore
    private Post post;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime timestamps;
}
