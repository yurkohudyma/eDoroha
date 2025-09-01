package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.dto.RouteDto;
import ua.hudyma.dto.RouteResponseDto;
import ua.hudyma.service.RouteService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class GradeController {
    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponseDto> getRoute
            (@RequestBody RouteDto routeDto){
        return ResponseEntity.ok(
                routeService.getRoute (routeDto));
    }
}
