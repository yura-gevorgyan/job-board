package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String token;
    private boolean activated;
    private boolean isDeleted;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private UserRole userRole;

}
