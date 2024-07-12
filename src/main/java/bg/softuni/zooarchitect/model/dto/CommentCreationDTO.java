package bg.softuni.zooarchitect.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreationDTO {
    @NotBlank
    private String text;
}
