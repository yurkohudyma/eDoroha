package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Grade;
import ua.hudyma.dto.GradeRequestDto;
import ua.hudyma.repository.GradeRepository;
import ua.hudyma.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class GradeService {
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public Grade saveGrade(GradeRequestDto gradeRequestDto) {
        var grade = new Grade();
        var user = userRepository
                .findById(gradeRequestDto.userId())
                .orElseThrow();
        grade.setUser(user);
        grade.setGradeStatus(gradeRequestDto.gradeStatus());
        grade.setDeparture(gradeRequestDto.departure());
        grade.setDestination(gradeRequestDto.destination());
        return gradeRepository.save(grade);
    }
}
