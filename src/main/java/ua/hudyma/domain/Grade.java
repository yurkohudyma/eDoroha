package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ua.hudyma.converter.RouteListConverter;
import ua.hudyma.enums.GradeStatus;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "grades")
@Data

public class Grade {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @CreationTimestamp
    private Date createdOn;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @UpdateTimestamp
    private Date updatedOn;
    @ManyToOne
    @JoinColumn(
            name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private GradeStatus gradeStatus;

    @Convert(converter = RouteListConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private List<double[]> routeList;


}
