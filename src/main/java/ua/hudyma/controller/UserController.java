package ua.hudyma.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.domain.User;
import ua.hudyma.enums.UserRequestDto;
import ua.hudyma.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> addUser (@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.addUser(userRequestDto));
    }

    @GetMapping
    public ResponseEntity<User> generateUser (){
        return ResponseEntity.ok(userService.generateUser());
    }

    @GetMapping("/{number}")
    public ResponseEntity<List<User>> generateUser (@PathVariable Integer number){
        return ResponseEntity.ok(userService.generateUser(number));
    }
}
