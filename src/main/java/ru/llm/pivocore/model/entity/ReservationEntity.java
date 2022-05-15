package ru.llm.pivocore.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @Column(name = "reservation_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_id_generator")
    @SequenceGenerator(name = "reservations_id_generator", allocationSize = 25)
    private Long id;

    @Column(name = "start_reservation_time")
    private Instant startReservationTime;

    @Column(name = "end_reservation_time")
    private Instant endReservationTime;

    @Column(name = "deposit")
    private Integer deposit;

    @Column(name = "amount_of_guests")
    private Short amountOfGuests;

    @Column(name = "approve_time")
    private Instant approve_time;

    @ManyToOne
    @JoinColumn(name="app_user_id")
    private AppUserEntity user;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name="approved_by")
    private RestaurantUserEntity restaurantUser;

    @Override
    public String toString() {
        return "ReservationEntity{" +
                "id=" + id +
                ", startReservationTime=" + startReservationTime +
                ", endReservationTime=" + endReservationTime +
                ", deposit=" + deposit +
                ", amountOfGuests=" + amountOfGuests +
                ", approve_time=" + approve_time +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationEntity that = (ReservationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user)
                && Objects.equals(startReservationTime, that.startReservationTime)
                && Objects.equals(endReservationTime, that.endReservationTime)
                && Objects.equals(deposit, that.deposit)
                && Objects.equals(amountOfGuests, that.amountOfGuests)
                && Objects.equals(approve_time, that.approve_time);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (startReservationTime != null ? startReservationTime.hashCode() : 0);
        result = 31 * result + (endReservationTime != null ? endReservationTime.hashCode() : 0);
        result = 31 * result + (deposit != null ? deposit.hashCode() : 0);
        result = 31 * result + (amountOfGuests != null ? amountOfGuests.hashCode() : 0);
        result = 31 * result + (approve_time != null ? approve_time.hashCode() : 0);
        return result;
    }
}

