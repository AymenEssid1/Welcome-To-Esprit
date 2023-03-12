package tn.esprit.springfever.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Log implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idLog;
    @Temporal(TemporalType.DATE)
    private Date dateLog;
    private double positive ;
    private double negative ;
    private double neutral ;
    private double compound ;





}
