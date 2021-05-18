package oana.dp.backend.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Recipe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "cannot be blank!")
    private String name;

    @NotBlank(message = "cannot be blank!")
    private String difficulty;

    @NotNull(message = "cannot be null!")
    private int time;

    @NotBlank(message = "cannot be blank!")
    private String ingredients;

    @NotBlank(message = "cannot be blank!")
    private String instructions;

}
