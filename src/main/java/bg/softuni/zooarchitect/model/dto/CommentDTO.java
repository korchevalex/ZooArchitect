package bg.softuni.zooarchitect.model.dto;

import bg.softuni.zooarchitect.model.entity.User;
import bg.softuni.zooarchitect.model.entity.Zoo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private long id;
    private User author;
    private Zoo zoo;
    private String text;
    private LocalDateTime time;
    private List<CommentDTO> replies;
    public String getShortTime() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(this.getTime());
    }
}

