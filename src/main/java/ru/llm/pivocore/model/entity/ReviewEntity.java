package ru.llm.pivocore.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @Column(name = "review_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", allocationSize = 1)
    private Long id;

    @Column(name = "text", nullable = false)
    private String comment;

    @Column(name = "score")
    private BigDecimal score;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_user_id", referencedColumnName = "app_user_id")
    private AppUserEntity appUser;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "restaurant_id")
    private RestaurantEntity restaurant;

}
