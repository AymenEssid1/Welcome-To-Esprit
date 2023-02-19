package tn.esprit.springfever.entities;

import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.*;


@Entity
@ToString
@Data
public class Role implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleType rolename;

}
