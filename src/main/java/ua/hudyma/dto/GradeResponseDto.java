package ua.hudyma.dto;

import ua.hudyma.enums.GradeStatus;

public record GradeResponseDto(
        GradeStatus gradeStatus,
        Long userId,
        RoutePoint departure,
        RoutePoint destination
) {
}
