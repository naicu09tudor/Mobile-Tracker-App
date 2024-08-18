package edu.tucn.scd.serverapp.terminal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Naicu Tudor
 */
@Service
@Slf4j
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    private final TerminalMapper terminalMapper = new TerminalMapper();


    @Transactional(rollbackFor = Exception.class)
    public TerminalDTO create(TerminalDTO terminalDTO) {
        Terminal terminal = terminalMapper.mapDtoToEntity(terminalDTO);
        TerminalDTO terminalDTOfind = findByTerminalId(terminalMapper.mapDtoToEntity(terminalDTO).getId());
        if(terminalDTOfind == null){
            log.info("Saved a new terminal with id {}", terminal.getId());
            return terminalMapper.mapEntityToDto(terminalRepository.save(terminal));
        }
        log.info("Terminal already exists.");
        return null;
    }

    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public TerminalDTO findByTerminalId(String terminalId) {
        Terminal terminal = terminalRepository.findTerminal(terminalId);
        return terminalMapper.mapEntityToDto(terminal);
    }

    @Transactional(rollbackFor = Exception.class)
    public TerminalDTO updateTerminal(String id, String terminalName) {
        Terminal existingTerminal = terminalRepository.findTerminal(id);
        existingTerminal.setTerminalName(terminalName);
        Terminal updatedTerminal = terminalRepository.save(existingTerminal);
        return terminalMapper.mapEntityToDto(updatedTerminal);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTerminal(String id) {
        terminalRepository.deleteById(id);
        log.info("Deleted terminal with id {}", id);
    }

}
