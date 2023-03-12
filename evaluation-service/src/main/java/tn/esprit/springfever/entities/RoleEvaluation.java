package tn.esprit.springfever.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.enums.ERole;

import javax.persistence.*;


import java.io.Serializable;


@Entity
@ToString
@Data


public class RoleEvaluation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;



    }