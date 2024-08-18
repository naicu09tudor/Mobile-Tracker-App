package edu.tucn.scd.serverapp.terminal;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.tucn.scd.serverapp.position.PositionDTO;
import lombok.Data;

import java.util.List;

/**
 * @author Naicu Tudor
 */
@Data
public class TerminalDTO {
    private String id;
    private String terminalName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PositionDTO> positions;

}