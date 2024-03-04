package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "company_picture")
@Data
public class CompanyPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @ManyToOne
    private Company company;


}
