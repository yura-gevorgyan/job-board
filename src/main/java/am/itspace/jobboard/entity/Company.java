package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "company")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String logoName;
    private String description;
    private String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundedDate;
    private String phone;
    private String website;
    private String email;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

}
