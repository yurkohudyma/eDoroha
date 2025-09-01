package ua.hudyma.dto;

import java.util.List;

public record RouteDto(
        RoutePoint departure,
        RoutePoint destination) {
}
