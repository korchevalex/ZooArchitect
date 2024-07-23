package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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

    @ManyToMany
    private Set<Animal> animals;

    @Column(nullable = false)
    private String imageURL;

    @OneToMany(mappedBy = "zoo")
    private List<Comment> comments;

    public Zoo() {
        this.animals = new HashSet<>();
        this.comments = new ArrayList<>();
    }

}
