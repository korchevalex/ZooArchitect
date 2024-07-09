package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToOne
    private User owner;

    @OneToMany(mappedBy = "zoo")
    private List<Animal> animals;
}
