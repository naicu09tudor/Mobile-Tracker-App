package edu.tucn.scd.serverapp.terminal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Naicu Tudor
 */
@RestController
@RequestMapping("/terminal")
public class TerminalController {
    @Autowired
    private TerminalService terminalService;

    @PostMapping
    @Operation(summary = "Save a new terminal")
    @SecurityRequirement(name = "Bearer Authentication")
    public TerminalDTO create(@RequestBody TerminalDTO terminalDTO) {
        return terminalService.create(terminalDTO);
    }

    @GetMapping
    @Operation(summary = "Get a terminal by id")
    @SecurityRequirement(name = "Bearer Authentication")
    public TerminalDTO findTerminal(@RequestParam String id) {
        return terminalService.findByTerminalId(id);
    }

    @PutMapping
    @Operation(summary = "Update an existing terminal by id")
    @SecurityRequirement(name = "Bearer Authentication")
    public TerminalDTO updateTerminal(@RequestParam String id, @RequestParam String terminalName) {
        return terminalService.updateTerminal(id, terminalName);
    }

    @DeleteMapping
    @Operation(summary = "Delete a terminal by id")
    @SecurityRequirement(name = "Bearer Authentication")
    public void deleteTerminal(@RequestParam String id) {
        terminalService.deleteTerminal(id);
    }
}
