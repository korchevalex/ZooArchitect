package bg.softuni.zooarchitect.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreationDTO {
    @NotBlank(message = "Text is mandatory")
    private String text;
}
