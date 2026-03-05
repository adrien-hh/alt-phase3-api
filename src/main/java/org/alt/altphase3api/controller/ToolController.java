package org.alt.altphase3api.controller;

import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.service.ToolService;
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
    public List<Tool> getTools() {
        return toolService.getAllTools();
    }

    @GetMapping("/{id}")
    public Tool getTool(@PathVariable Integer id) {
        return toolService.getToolById(id);
    }

    @PostMapping
    public Tool createTool(@RequestBody Tool tool) {
        return toolService.createTool(tool);
    }

    @PutMapping("/{id}")
    public Tool updateTool(@PathVariable Integer id, @RequestBody Tool tool) {
        return toolService.updateTool(id, tool);
    }
}