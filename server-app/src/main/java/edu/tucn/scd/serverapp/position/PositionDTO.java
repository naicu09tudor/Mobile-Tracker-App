package edu.tucn.scd.serverapp.position;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.tucn.scd.serverapp.terminal.Terminal;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

/**
 * @author Radu Miron
 * @version 1
 */

@Data // lombok annotation, creates equals(), hashCode() and toString at compile time
public class PositionDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // hides field 'id' for create in swagger
    private Integer id;

    private String latitude;
    private String longitude;

    private String terminalId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // hides field 'creationDate' for create in swagger
    private Date creationDate;
}
