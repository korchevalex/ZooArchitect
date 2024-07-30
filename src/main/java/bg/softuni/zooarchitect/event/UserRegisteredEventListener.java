package bg.softuni.zooarchitect.event;

import bg.softuni.zooarchitect.model.dto.UserRegisterDTO;
import bg.softuni.zooarchitect.service.MailService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventListener implements ApplicationListener<UserRegisteredEvent> {
    private final MailService mailService;

    public UserRegisteredEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        UserRegisterDTO userRegisterDTO = event.getUserRegisterDTO();

        mailService.sendRegistrationEmail(userRegisterDTO.getEmail(), userRegisterDTO.getUsername());
    }
}
