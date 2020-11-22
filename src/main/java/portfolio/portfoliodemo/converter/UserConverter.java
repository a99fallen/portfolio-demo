package portfolio.portfoliodemo.converter;

import org.springframework.stereotype.Component;
import portfolio.portfoliodemo.data.user.UserSummary;
import portfolio.portfoliodemo.domain.model.User;
import portfolio.portfoliodemo.domain.model.UserDetails;
import portfolio.portfoliodemo.web.command.RegisterUserCommand;

@Component
public class UserConverter {

    public User from(RegisterUserCommand registerUserCommand) {
        return User.builder()
                .username(registerUserCommand.getUsername())
                .password(registerUserCommand.getPassword())
                .build();
    }

    public UserSummary toUserSummary(User user) {
        UserDetails details = new UserDetails();
        return UserSummary.builder()
                .username(user.getUsername())
                .firstName(details.getFirstName())
                .lastName(details.getLastName())
                .birthDate(details.getBirthDate())
                .build();
    }
}
