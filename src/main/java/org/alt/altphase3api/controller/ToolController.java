package org.alt.altphase3api.controller;

import jakarta.validation.Valid;
import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.dto.CreateToolRequest;
import org.alt.altphase3api.dto.UpdateToolRequest;
import org.alt.altphase3api.service.ToolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final ToolService toolService;

    public ToolController(ToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    public ResponseEntity<List<Tool>> getTools() {
        return ResponseEntity.ok(toolService.getAllTools());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tool> getTool(@PathVariable Integer id) {
        return ResponseEntity.ok(toolService.getToolById(id));
    }

    @PostMapping
    public ResponseEntity<Tool> createTool(@Valid @RequestBody CreateToolRequest request) {
        Tool tool = toolService.createTool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tool);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tool> updateTool(@PathVariable Integer id, @Valid @RequestBody UpdateToolRequest request) {
        Tool tool = toolService.updateTool(id, request);
        return ResponseEntity.ok(tool);
    }
}