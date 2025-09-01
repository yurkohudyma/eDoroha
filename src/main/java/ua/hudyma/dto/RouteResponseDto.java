package ua.hudyma.dto;

import java.util.List;

public record RouteResponseDto(
        RouteDto routeDto,
        List<double[]> routePoints
) {
}
