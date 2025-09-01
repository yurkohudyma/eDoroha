package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.repository.GradeRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class GradeService {
    private final GradeRepository gradeRepository;
}
