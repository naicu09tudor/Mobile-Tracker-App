package edu.tucn.scd.serverapp.position;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Radu Miron
 * @version 1
 */
@Service // creates one instance of this class
@Slf4j // adds the 'log(ger)' instance to this class
public class PositionService {

    @Autowired // injects the PositionRepository instance
    private PositionRepository positionRepository;
    @Autowired // injects the PositionMapper instance
    private PositionMapper positionMapper;

    @Transactional(rollbackFor = Exception.class) // the method is executed in a transaction
    public PositionDTO create(PositionDTO positionDTO) {
        PositionDTO createdPositionDTO =
                positionMapper.positionToDto(positionRepository.save(positionMapper.dtoToPosition(positionDTO)));

        log.info("Saved a new position for terminal with id " + createdPositionDTO.getTerminalId());

        return createdPositionDTO;
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true) // the method is executed in a read-only transaction
    public List<PositionDTO> findByTerminalIdStartDateEndDate(String terminalId, Date startDate, Date endDate) {
        List<PositionDTO> positions = positionRepository.findPositions(terminalId, startDate, endDate)
                .stream()
                .map(p -> positionMapper.positionToDto(p))
                .collect(Collectors.toList());

        log.info(String.format("Found %d positions for: terminalId=%s, startDate=%s, endDate=%s",
                positions.size(), terminalId, startDate, endDate));

        return positions;
    }


    @Transactional(rollbackFor = Exception.class, readOnly = true) // the method is executed in a read-only transaction
    public List<PositionDTO> findByTerminalId(String terminalId) {
        List<PositionDTO> positions = positionRepository.findPositionsByTerminal(terminalId)
                .stream()
                .map(p -> positionMapper.positionToDto(p))
                .collect(Collectors.toList());

        log.info(String.format("Found %d positions for: terminalId=%s",
                positions.size(), terminalId));

        return positions;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(String terminalId, Integer id) {
        Position position = positionRepository.findPositionsByTerminalAndId(terminalId,id);
        positionRepository.delete(position);
    }

    @Transactional(rollbackFor = Exception.class)
    public PositionDTO updatePosition(Integer id, String latitude, String longitude) {
        Position existingPosition = positionRepository.findPositionById(id);

        existingPosition.setLatitude(latitude);
        existingPosition.setLongitude(longitude);

        Position updatedPosition = positionRepository.save(existingPosition);

        return positionMapper.positionToDto(updatedPosition);
    }
}
