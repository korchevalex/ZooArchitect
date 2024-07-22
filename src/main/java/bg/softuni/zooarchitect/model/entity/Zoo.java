package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "zoos")
public class Zoo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @OneToOne
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Animal> animals;

    @Column(nullable = false)
    private String imageURL;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "zoo")
    private List<Comment> comments;

    public Zoo() {
        this.animals = new HashSet<>();
        this.comments = new ArrayList<>();
    }
}
