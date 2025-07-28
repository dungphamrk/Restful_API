package ra.ss14.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "showtimes")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class ShowTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false, columnDefinition = "CHAR", length = 3)
    private String room;
}
