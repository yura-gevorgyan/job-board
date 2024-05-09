package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_wishlist")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobWishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Job job;

    @ManyToOne
    private User user;

}
