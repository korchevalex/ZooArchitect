package bg.softuni.zooarchitect.event;


import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private final UserRegisterDTO userRegisterDTO;

    public UserRegisteredEvent(Object source, UserRegisterDTO userRegisterDTO) {
        super(source);
        this.userRegisterDTO = userRegisterDTO;
    }
}
