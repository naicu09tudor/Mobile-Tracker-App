package edu.tucn.scd.serverapp.terminal;

import edu.tucn.scd.serverapp.exceptions.NotFoundException;
import edu.tucn.scd.serverapp.position.Position;
import edu.tucn.scd.serverapp.position.PositionDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Naicu Tudor
 */
public class TerminalMapper {

    @Autowired
    private TerminalRepository terminalRepository;

    public Terminal mapDtoToEntity(TerminalDTO terminalDTO) {
        if (terminalDTO == null) {
            return null;
        }

        Terminal terminal = new Terminal();
        terminal.setId(terminalDTO.getId());
        terminal.setTerminalName(terminalDTO.getTerminalName());
        terminal.setPositions(mapPositionDTOListToEntity(terminalDTO.getPositions()));
        return terminal;
    }

    public TerminalDTO mapEntityToDto(Terminal terminal) {
        if (terminal == null) {
            return null;
        }

        TerminalDTO terminalDTO = new TerminalDTO();
        terminalDTO.setId(terminal.getId());
        terminalDTO.setTerminalName(terminal.getTerminalName());
        terminalDTO.setPositions(mapPositionEntityListToDTO(terminal.getPositions()));
        return terminalDTO;
    }

    public Position mapPositionDtoToEntity(PositionDTO positionDTO) {
        if (positionDTO == null) {
            return null;
        }

        Position position = new Position();
        position.setId(positionDTO.getId());
        position.setLatitude(positionDTO.getLatitude());
        position.setLongitude(positionDTO.getLongitude());
        position.setCreationDate(positionDTO.getCreationDate());
        position.setTerminal(terminalRepository.findById(positionDTO.getTerminalId())
                .orElseThrow(() -> new NotFoundException("Terminal with id '" + positionDTO.getTerminalId() + "' does not exist")));
        return position;
    }

    public PositionDTO mapPositionEntityToDto(Position position) {
        if (position == null) {
            return null;
        }

        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setLatitude(position.getLatitude());
        positionDTO.setLongitude(position.getLongitude());
        positionDTO.setCreationDate(position.getCreationDate());
        positionDTO.setTerminalId(position.getTerminal().getId());
        return positionDTO;
    }

    protected List<Position> mapPositionDTOListToEntity(List<PositionDTO> positionDTOs) {
        if (positionDTOs == null) {
            return new ArrayList<>(); // or handle the null case based on your logic
        } else {
            return positionDTOs.stream()
                    .map(this::mapPositionDtoToEntity)
                    .collect(Collectors.toList());
        }
    }


    protected List<PositionDTO> mapPositionEntityListToDTO(List<Position> positions) {
        return positions.stream()
                .map(this::mapPositionEntityToDto)
                .collect(Collectors.toList());
    }
}
