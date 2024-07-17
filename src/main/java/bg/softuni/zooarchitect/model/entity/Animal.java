package bg.softuni.zooarchitect.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String imageURL;

    @ManyToOne
    private Habitat habitat;

    public boolean hasHabitat() {
        return this.habitat != null;
    }
}
