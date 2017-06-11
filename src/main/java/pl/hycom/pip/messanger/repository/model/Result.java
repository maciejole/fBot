package pl.hycom.pip.messanger.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Piotr on 2017-06-05.
 */
@Data
@Entity
@Table(name = "RESULT")
public class Result implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column
    private String base;

    @NotNull
    @Column
    private  String result;



}
