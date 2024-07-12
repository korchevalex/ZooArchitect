package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "zoos")
public class Zoo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    String name;

    String description;

    @OneToOne
    private User owner;

    @OneToMany(mappedBy = "zoo")
    private List<Animal> animals;

    @Column(nullable = false)
    private String imageURL;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "zoo")
    private List<Comment> comments;

    public Zoo() {
        this.animals = new ArrayList<>();
        this.comments = new ArrayList<>();
    }
}
