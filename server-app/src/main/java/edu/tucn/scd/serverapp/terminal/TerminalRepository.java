package edu.tucn.scd.serverapp.terminal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Naicu Tudor
 */
public interface TerminalRepository extends CrudRepository<Terminal, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM terminal WHERE id=?1")
    Terminal findTerminal(String id);
}
