package ua.hudyma.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.domain.Grade;
import ua.hudyma.dto.GradeRequestDto;
import ua.hudyma.dto.GradeResponseDto;
import ua.hudyma.dto.RouteDto;
import ua.hudyma.dto.RouteResponseDto;
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
            (@RequestBody RouteDto routeDto){
        return ResponseEntity.ok(
                routeService.getRoute (routeDto));
    }

    @PostMapping("/save")
    public ResponseEntity<GradeRequestDto> saveGrade (
            @RequestBody GradeRequestDto gradeRequestDto) {
        gradeService.saveGrade(gradeRequestDto);
        return ResponseEntity.ok(gradeRequestDto);
    }

    @GetMapping
    public List<GradeResponseDto> getAllGrades (){
        return gradeService.getAll();
    }

}
