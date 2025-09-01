package ua.hudyma.enums;

import lombok.RequiredArgsConstructor;

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
}
