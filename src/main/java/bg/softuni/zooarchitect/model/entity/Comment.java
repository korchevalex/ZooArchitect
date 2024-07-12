package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToMany
    private List<Comment> replies;

    public Comment() {
        this.replies = new ArrayList<>();
    }
}
