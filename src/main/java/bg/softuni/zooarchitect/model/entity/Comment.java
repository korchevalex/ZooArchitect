package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User author;

    @ManyToOne
    private Zoo zoo;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime time;
}
