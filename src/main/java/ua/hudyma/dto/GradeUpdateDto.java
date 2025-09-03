package ua.hudyma.dto;

import ua.hudyma.enums.GradeStatus;

import java.util.List;

public record GradeUpdateDto(
        Long gradeId,
        GradeStatus newGradeStatus
) {
}
