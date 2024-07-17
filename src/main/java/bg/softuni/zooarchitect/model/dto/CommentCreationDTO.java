package bg.softuni.zooarchitect.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentCreationDTO {
    @NotBlank(message = "Text is mandatory")
    private String text;
}
