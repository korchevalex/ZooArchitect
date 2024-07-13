package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Data
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

    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> replies;

    public Comment() {
        this.replies = new ArrayList<>();
    }

    public String getShortTime() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(this.getTime());
    }
}
