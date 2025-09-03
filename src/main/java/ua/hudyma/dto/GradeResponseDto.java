package ua.hudyma.dto;

import ua.hudyma.enums.GradeStatus;

import java.util.List;

public record GradeResponseDto(
        Long gradeId,
        GradeStatus gradeStatus,
        Long userId,
        List<double[]> routeList
) {
}
