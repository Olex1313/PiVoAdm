package ru.llm.pivocore.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_tables")
public class RestaurantTableEntity {
    @Id
    @Column(name="table_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_tables_id_generator")
    @SequenceGenerator(name = "restaurant_tables_id_generator", allocationSize = 25)
    private Long id;
    @Column(name = "table_num")
    private Short tableNum;
    @Column(name = "max_amount")
    private Short maxAmount;
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private RestaurantEntity restaurant;

    @OneToOne
    @JoinColumn(name = "table_id")
    private ReservationEntity reservation;

}
