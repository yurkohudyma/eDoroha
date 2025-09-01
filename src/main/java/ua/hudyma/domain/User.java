package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @CreationTimestamp
    private Date createdOn;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @UpdateTimestamp
    private Date updatedOn;
    @OneToMany(mappedBy = "user",
            fetch = FetchType.EAGER)
    private List<Grade> gradeList;
}