package edu.tucn.scd.serverapp.position;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Radu Miron
 * @version 1
 */
@RestController // creates an instance of the current class
@RequestMapping("/positions") // maps the requests starting with '/positions' to this controller
public class PositionController {

    @Autowired // creates the injection of PositionService instance
    private PositionService positionService;

    @PostMapping // maps the '/positions' POST requests to this method
    @Operation(summary = "Save a new position") // swagger description
    public PositionDTO create(@RequestBody PositionDTO positionDTO) {
        return positionService.create(positionDTO);
    }

    @GetMapping // maps the '/positions' GET requests to this method
    @Operation(summary = "Get all positions for a terminal between two dates") // swagger description
    @SecurityRequirement(name = "Bearer Authentication") // swagger auth
    public List<PositionDTO> findPositions(@RequestParam String terminalId,
                                           @RequestParam Date startDate,
                                           @RequestParam Date endDate) {
        return positionService.findByTerminalIdStartDateEndDate(terminalId, startDate, endDate);
    }

    @GetMapping("/{terminalId}") // maps the '/positions' GET requests to this method
    @Operation(summary = "Get all positions for a terminal") // swagger description
    @SecurityRequirement(name = "Bearer Authentication") // swagger auth
    public List<PositionDTO> findPositions(@RequestParam String terminalId) {
        return positionService.findByTerminalId(terminalId);
    }

    @DeleteMapping() // maps the '/positions' DELETE to this method
    @Operation(summary = "Delete a position by id and terminalId") // swagger description
    @SecurityRequirement(name = "Bearer Authentication") // swagger auth
    public void deletePosition(@RequestParam String terminalId,
                               @RequestParam Integer id) {
        positionService.deletePosition(terminalId,id);
    }

    @PutMapping()
    @Operation(summary = "Update an existing position") // swagger description
    @SecurityRequirement(name = "Bearer Authentication") // swagger auth
    public PositionDTO updatePosition(@RequestParam Integer id,
                                      @RequestParam String latitude,
                                      @RequestParam String longitude) {
        return positionService.updatePosition(id,latitude,longitude);
    }
}
