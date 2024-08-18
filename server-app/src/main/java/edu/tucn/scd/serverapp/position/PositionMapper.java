package edu.tucn.scd.serverapp.position;

import edu.tucn.scd.serverapp.terminal.TerminalRepository;
import edu.tucn.scd.serverapp.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Radu Miron
 * @version 1
 */

@Component // creates one instance of this class
@Slf4j // adds the 'log(ger)' instance to this class
public class PositionMapper {

    @Autowired // creates the injection of the TerminalRepository instance
    private TerminalRepository terminalRepository;

    public PositionDTO positionToDto(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setLatitude(position.getLatitude());
        positionDTO.setLongitude(position.getLongitude());
        positionDTO.setCreationDate(position.getCreationDate());
        positionDTO.setTerminalId(position.getTerminal().getId());
        return positionDTO;
    }

    public Position dtoToPosition(PositionDTO positionDTO) {
        Position position = new Position();
        position.setId(positionDTO.getId());
        position.setLatitude(positionDTO.getLatitude());
        position.setLongitude(positionDTO.getLongitude());
        position.setCreationDate(positionDTO.getCreationDate());

        // find the terminal by id and set it on position
        position.setTerminal(terminalRepository.findById(positionDTO.getTerminalId())
                                               .orElseThrow(() -> {
                                                   var errMsg = "Terminal with id '" + positionDTO.getTerminalId() + "' does not exist";
                                                   log.error(errMsg);
                                                   return new NotFoundException(errMsg);
                                               }));

        return position;
    }
}
