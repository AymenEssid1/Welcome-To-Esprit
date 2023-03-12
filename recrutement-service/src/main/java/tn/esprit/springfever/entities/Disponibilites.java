package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@ToString
@Data

public class Disponibilites implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id ;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
     private LocalDateTime preferDateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_rdv_id")
    @JsonIgnore
    private Job_RDV jobRDV;

}
