package edu.tucn.scd.serverapp.position;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Radu Miron
 * @version 1
 */
public interface PositionRepository extends JpaRepository<Position, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM position WHERE terminal_id=?1" +
            " AND creation_date BETWEEN ?2 AND ?3")
    List<Position> findPositions(String terminalId, Date sd, Date ed);

    @Query(nativeQuery = true, value = "SELECT * FROM position WHERE terminal_id=?1")
    List<Position> findPositionsByTerminal(String terminalId);

    @Query(nativeQuery = true, value = "SELECT * FROM position WHERE terminal_id=?1 AND id=?2")
    Position findPositionsByTerminalAndId(String terminalId, Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM position WHERE id=?1")
    Position findPositionById(Integer id);
}
