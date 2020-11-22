package portfolio.portfoliodemo.converter;

import org.springframework.stereotype.Component;
import portfolio.portfoliodemo.domain.model.User;
import portfolio.portfoliodemo.web.command.RegisterUserCommand;

@Component
public class UserConverter {

    public User from(RegisterUserCommand registerUserCommand) {
        return User.builder()
                .username(registerUserCommand.getUsername())
                .password(registerUserCommand.getPassword())
                .build();
    }
}
