package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resume_wishlist")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeWishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Resume resume;

    @ManyToOne
    private User user;
}
