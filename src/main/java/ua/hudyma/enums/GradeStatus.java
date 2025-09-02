package ua.hudyma.enums;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public enum GradeStatus {

    EXCELLENT(5),
    GOOD(4),
    FAIR(3),
    BAD(2),
    AWFUL(1);

    private final int grade;

    public int getGrade() {
        return grade;
    }
    public GradeStatus getGradeEnum(int grade) {
        return Arrays
                .stream(GradeStatus.values())
                .filter(g -> getGrade() == grade)
                .findFirst()
                .orElseThrow();
    }

}
