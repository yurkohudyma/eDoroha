package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.Grade;
import ua.hudyma.dto.GradeRequestDto;
import ua.hudyma.dto.GradeResponseDto;
import ua.hudyma.repository.GradeRepository;
import ua.hudyma.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class GradeService {
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public List<GradeResponseDto> getAll() {
        return gradeRepository.findAll().stream()
                .map(grade -> new GradeResponseDto(
                        grade.getGradeStatus(),
                        grade.getUser().getId(),
                        grade.getRouteList()
                ))
                .toList();
    }

    public GradeRequestDto saveGrade(GradeRequestDto gradeRequestDto) {
        var grade = new Grade();
        var user = userRepository
                .findById(gradeRequestDto.userId())
                .orElseThrow();
        grade.setUser(user);
        grade.setGradeStatus(gradeRequestDto.gradeStatus());
        grade.setRouteList(gradeRequestDto.routeList());
        gradeRepository.save(grade);
        return gradeRequestDto;
    }
}
