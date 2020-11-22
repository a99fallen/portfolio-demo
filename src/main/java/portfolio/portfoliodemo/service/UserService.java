package portfolio.portfoliodemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.portfoliodemo.converter.UserConverter;
import portfolio.portfoliodemo.domain.model.User;
import portfolio.portfoliodemo.domain.repository.UserRepository;
import portfolio.portfoliodemo.exception.UserAlreadyExistsException;
import portfolio.portfoliodemo.web.command.RegisterUserCommand;

import javax.transaction.Transactional;
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

        userToCreate.setActive(Boolean.TRUE);
        userToCreate.setRoles(Set.of("ROLE_USER"));
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userRepository.save(userToCreate);
        log.debug("Zapisany użytkownik: {}", userToCreate);

        return userToCreate.getId();
    }
}
