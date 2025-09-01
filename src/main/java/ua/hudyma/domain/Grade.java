package ua.hudyma.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "grades")
@Data

public class Grade {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;


}
