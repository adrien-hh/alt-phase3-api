package org.alt.altphase3api.service;

import org.alt.altphase3api.domain.bo.Tool;
import org.alt.altphase3api.repository.ToolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {

    private final ToolRepository toolRepository;

    public ToolService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public List<Tool> getAllTools() {
        return toolRepository.findAll();
    }

    public Tool getToolById(Integer id) {
        return toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));
    }

    public Tool createTool(Tool tool) {
        return toolRepository.save(tool);
    }

    public Tool updateTool(Integer id, Tool updatedTool) {
        Tool tool = getToolById(id);

        if (updatedTool.getDescription() != null)
            tool.setDescription(updatedTool.getDescription());

        if (updatedTool.getMonthlyCost() != null)
            tool.setMonthlyCost(updatedTool.getMonthlyCost());

        if (updatedTool.getStatus() != null)
            tool.setStatus(updatedTool.getStatus());

        return toolRepository.save(tool);
    }
}
