package tn.esprit.springfever.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import tn.esprit.springfever.enums.Channel;
import tn.esprit.springfever.enums.TypeOfAudience;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Advertisement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Ad implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private int initialTargetNumberOfViews;
    private float cost;
    @Enumerated(EnumType.STRING)
    private TypeOfAudience typeOfAudience;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ad")
    private List<AdMedia> media;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ad")
    private List<AdViews> views;

}