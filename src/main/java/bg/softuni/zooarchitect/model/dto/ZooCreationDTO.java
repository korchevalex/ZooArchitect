package bg.softuni.zooarchitect.model.dto;

import bg.softuni.zooarchitect.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ZooCreationDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    User owner;

    @NotBlank(message = "Image URL is mandatory")
    private String imageURL;

    private String description;
}
