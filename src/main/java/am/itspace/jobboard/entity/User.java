package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String surname;

    @Email(message = "Invalid Email")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String token;
    private boolean activated;
    private boolean isDeleted;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerDate;
}
