package ua.hudyma.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ua.hudyma.dto.RouteDto;
import ua.hudyma.dto.RouteResponseDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@ConfigurationProperties(prefix = "graphhopper.api")
public class RouteService {

    private static final RestTemplate restTemplate = new RestTemplate();
    //private static String staticKey;
    @Value("${graphhopper.api.key}")
    private String key;

    public RouteResponseDto getRoute(RouteDto routeDto) {
        var departure = routeDto.departure();
        var destination = routeDto.destination();
        String url = UriComponentsBuilder
                .fromHttpUrl("https://graphhopper.com/api/1/route")
                .queryParam("point", departure.latitude() + "," + departure.longitude())
                .queryParam("point", destination.latitude() + "," + destination.longitude())
                .queryParam("vehicle", "car")
                .queryParam("locale", "uk")
                .queryParam("points_encoded", "false")
                .queryParam("calc_points", "true")
                .queryParam("key", key)
                .toUriString();

        var response = restTemplate
                .getForEntity(url, JsonNode.class);

        var points = response.getBody()
                .get("paths")
                .get(0)
                .get("points")
                .get("coordinates");

        List<double[]> routePointsCoordsList = new ArrayList<>();
        for (JsonNode point : points) {
            double lon = point.get(0).asDouble();
            double lat = point.get(1).asDouble();
            routePointsCoordsList.add(new double[]{lat, lon});
        }

        return new RouteResponseDto(
                routeDto,
                routePointsCoordsList);
    }
}
