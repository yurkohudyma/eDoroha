package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ua.hudyma.converter.RouteListConverter;
import ua.hudyma.dto.RouteDto;
import ua.hudyma.dto.RoutePoint;
import ua.hudyma.enums.GradeStatus;

import java.math.BigDecimal;
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
    /*@Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude",
                    column = @Column(name = "departure_latitude", nullable = false)),
            @AttributeOverride(name = "longitude",
                    column = @Column(name = "departure_longitude", nullable = false))
    })
    private RoutePoint departure;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude",
                    column = @Column(name = "destination_latitude", nullable = false)),
            @AttributeOverride(name = "longitude",
                    column = @Column(name = "destination_longitude", nullable = false))
    })
    private RoutePoint destination;*/

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private GradeStatus gradeStatus;

    @Convert(converter = RouteListConverter.class)
    @Column(columnDefinition = "LONGTEXT")
    private List<double[]> routeList;


}
