package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.hudyma.dto.*;
import ua.hudyma.service.GradeService;
import ua.hudyma.service.RouteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class GradeController {
    private final RouteService routeService;
    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<RouteResponseDto> getRoute
            (@RequestBody RouteDto routeDto) {
        return ResponseEntity.ok(
                routeService.getRoute(routeDto));
    }

    @PostMapping("/save")
    public ResponseEntity<GradeResponseDto> saveGrade(
            @RequestBody GradeRequestDto gradeRequestDto) {
        var grade = gradeService.saveGrade(gradeRequestDto);
        return ResponseEntity.ok(new GradeResponseDto(
                grade.getId(),
                grade.getGradeStatus(),
                grade.getUser().getId(),
                grade.getRouteList()));
    }

    @GetMapping
    public List<GradeResponseDto> getAllGrades() {
        return gradeService.getAll();
    }

    @PatchMapping
    public ResponseEntity<?> updateCurrentGrade(
            @RequestBody GradeUpdateDto dto) {
        gradeService.updateGrade(dto);
        log.info("---> Grade {} updated", dto.gradeId());
        return ResponseEntity.ok().build();
    }
}
