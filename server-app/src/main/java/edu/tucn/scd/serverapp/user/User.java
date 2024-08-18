package edu.tucn.scd.serverapp.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @author Naicu Tudor
 */
@Entity// maps class to DB table, object to table row, attribute to column
@Data // lombok annotation, creates equals(), hashCode() and toString at compile time
public class User {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Integer id;

    private String username;
    private String password;
}
