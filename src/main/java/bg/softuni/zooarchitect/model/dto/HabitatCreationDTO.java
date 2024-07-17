package bg.softuni.zooarchitect.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabitatCreationDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    private String description;

    @NotNull(message = "Latitude is mandatory")
    @PositiveOrZero(message = "Latitude must be valid")
    private Double latitude;

    @NotNull(message = "Longitude is mandatory")
    @PositiveOrZero(message = "Longitude must be valid")
    private Double longitude;

    @NotBlank(message = "Image URL is mandatory")
    private String imageURL;
}
