package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;


@Entity
@ToString
@Data
@ApiModel(description = "Badge object that includes a QR code")
public class Badge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long badgeid;


    @ApiModelProperty(notes = "The QR code image in base64-encoded PNG format", value = "iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAIAAAAC64paAAAAA3NCSVQICAjb4U/gAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAJZJREFUeNpiYBhgeP//PwMWQAKcAizF+/PjEwMzAxHgIYGZiYmZiYs3AhLCT//37LnQUyNiAeTbQ2MLFiwFEly6UxLihgZ2YAKXl6VdnBzMQl/CwMDwGJibGoqalBVMxh3NhYGDZfPmLXz/z5PhgcG5gCIwN7AOAC8gP4VhjEG2xgAAEI0F5fSNA5kwAAAABJRU5ErkJggg==")
    @Column(unique = true, nullable = false)
    private String qrCode;


    @JsonIgnore
    @OneToOne
    private User user;



    public Badge() {
        // Generate the QR code using a random UUID
        this.qrCode = UUID.randomUUID().toString();
    }



}
