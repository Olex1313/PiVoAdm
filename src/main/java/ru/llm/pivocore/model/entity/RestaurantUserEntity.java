package ru.llm.pivocore.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant_users")
public class RestaurantUserEntity {
    @Id
    @Column(name="restaurant_user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_user_id_generator")
    @SequenceGenerator(name = "restaurant_user_id_generator", allocationSize = 25)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="middle_name")
    private String middleName;

    @Column(name="username")
    private String username;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="email")
    private String email;

    @Column(name="is_active")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurants_restaurant_users",
            joinColumns = { @JoinColumn(name = "restaurant_user_id") },
            inverseJoinColumns = { @JoinColumn(name = "restaurant_id") }
    )
    private List<RestaurantEntity> restaurantList;

    @OneToMany
    @JoinColumn(name="approved_by")
    private List<ReservationEntity> reservations;

    @Override
    public String toString() {
        return "RestaurantUserEntity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", userName='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantUserEntity that = (RestaurantUserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(middleName, that.middleName) && Objects.equals(username, that.username) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(passwordHash, that.passwordHash) && Objects.equals(email, that.email) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        return result;
    }
}
