package ua.hudyma.dto;

import ua.hudyma.enums.GradeStatus;

import java.util.List;

public record GradeRequestDto(
        GradeStatus gradeStatus,
        Long userId,
        RoutePoint departure,
        RoutePoint destination,
        List<double[]> routeList) {
}
