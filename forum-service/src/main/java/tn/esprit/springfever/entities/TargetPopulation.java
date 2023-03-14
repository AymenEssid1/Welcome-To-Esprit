package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TargetPopulation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class TargetPopulation implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String name;
    @OneToOne
    @JsonIgnore
    private Ad ad;
}
