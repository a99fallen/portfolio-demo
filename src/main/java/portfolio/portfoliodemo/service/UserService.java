package portfolio.portfoliodemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.portfoliodemo.converter.UserConverter;
import portfolio.portfoliodemo.data.user.UserSummary;
import portfolio.portfoliodemo.domain.model.User;
import portfolio.portfoliodemo.domain.model.UserDetails;
import portfolio.portfoliodemo.domain.repository.UserRepository;
import portfolio.portfoliodemo.exception.UserAlreadyExistsException;
import portfolio.portfoliodemo.web.command.EditUserCommand;
import portfolio.portfoliodemo.web.command.RegisterUserCommand;

import java.util.Set;

@Service @Transactional
@Slf4j @RequiredArgsConstructor
public class UserService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long create(RegisterUserCommand registerUserCommand) {
        log.debug("Dane użytkownika do zapisania: {}", registerUserCommand);

        User userToCreate = userConverter.from(registerUserCommand);
        log.debug("Uzyskany obiekt użytkownika do zapisu: {}", userToCreate);
        if (userRepository.existsByUsername(userToCreate.getUsername())) {
            log.debug("Próba rejestracji na istniejącego użytkownika");
            throw new UserAlreadyExistsException(String.format("Użytkownik %s już istnieje", userToCreate.getUsername()));
        }

        setEncodedPassword(userToCreate);
        setDefaultData(userToCreate);
        userRepository.save(userToCreate);
        log.debug("Zapisany użytkownik: {}", userToCreate);

        return userToCreate.getId();
    }

    private void setDefaultData(User userToCreate) {
        setDefaultActive(userToCreate);
        setDefaultRole(userToCreate);
        setDefaultDetails(userToCreate);
    }

    private void setDefaultDetails(User userToCreate) {
        userToCreate.setDetails(UserDetails.builder()
                .user(userToCreate)
                .build());
    }

    private void setEncodedPassword(User userToCreate) {
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
    }

    private void setDefaultRole(User userToCreate) {
        userToCreate.setRoles(Set.of("ROLE_USER"));
    }

    private void setDefaultActive(User userToCreate) {
        userToCreate.setActive(Boolean.TRUE);
    }

    public UserSummary getCurrentUserSummary() {
        log.debug("Pobieranie danych użytkownika aktualnie zalogowanego");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.getAuthenticatedUser(username);
        UserSummary summary = userConverter.toUserSummary(user);
        log.debug("Podsumowanie danych użytkownika: {}", summary);

        return summary;
    }

    public boolean edit(EditUserCommand editUserCommand) {
        log.debug("Dane do edycji użytkownika: {}", editUserCommand);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getAuthenticatedUser(username);
        log.debug("Edycja użytkownika: {}", user);

        user = userConverter.from(editUserCommand, user);
        log.debug("Zmodyfikowane dane użytkownika: {}", user.getDetails());
        return true;
    }
}
