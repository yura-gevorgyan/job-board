package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_wishlist")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyWishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Company company;

    @ManyToOne
    private User user;
}
