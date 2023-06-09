package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "AdView")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class AdViews implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private Long user;
    @ManyToOne
    @JsonIgnore
    private Ad ad;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME )
    private LocalDateTime timestamps;

    public AdViews(Long user, Ad ad, LocalDateTime timestamps){
        this.user = user;
        this.ad=ad;
        this.timestamps= timestamps;
    }
}
