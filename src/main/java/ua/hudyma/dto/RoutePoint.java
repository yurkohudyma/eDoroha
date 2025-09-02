package ua.hudyma.dto;

import jakarta.persistence.Embeddable;

public record RoutePoint(
        double latitude,
        double longitude) {
}
