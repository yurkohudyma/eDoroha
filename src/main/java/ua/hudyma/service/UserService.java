package ua.hudyma.service;

import com.devskiller.jfairy.Fairy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.User;
import ua.hudyma.enums.UserRequestDto;
import ua.hudyma.repository.UserRepository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    public User addUser(UserRequestDto userRequestDto) {
        var user = new User();
        var email = userRequestDto.email();
        if (email != null) {
            user.setEmail(email);
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("EMAIL not provided");
    }

    public User generateUser() {
        Fairy fairy = Fairy.create();
        var generatedPerson = fairy.person();
        log.info(generatedPerson);
        var user = new User();
        user.setEmail(generatedPerson.getEmail());
        return userRepository.save(user);
    }

    public List<User> generateUser(int number) {
        var list = Stream
                .generate(UserService::create)
                .limit(number)
                .toList();
        return userRepository.saveAll(list);
    }

    @NotNull
    private static User create() {
        Fairy fairy = Fairy.create(Locale.forLanguageTag("uk"));
        var generatedPerson = fairy.person();
        log.info(generatedPerson);
        var user = new User();
        user.setEmail(generatedPerson.getEmail());
        return user;
    }
}
