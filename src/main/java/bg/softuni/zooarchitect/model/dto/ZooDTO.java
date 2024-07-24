package bg.softuni.zooarchitect.model.dto;

import bg.softuni.zooarchitect.model.entity.Animal;
import bg.softuni.zooarchitect.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ZooDTO {
    private long id;
    private String name;
    String description;
    private User owner;
    private Set<Animal> animals;
    private String imageURL;
    private List<CommentDTO> comments;
}
