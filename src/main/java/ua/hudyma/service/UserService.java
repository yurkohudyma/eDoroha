package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.User;
import ua.hudyma.enums.UserRequestDto;
import ua.hudyma.repository.UserRepository;

import javax.management.AttributeNotFoundException;

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
}
