package ru.llm.pivocore.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cuisines")
public class CuisineEntity {

    @Id
    @Column(name = "cuisine_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuisine_seq_generator")
    @SequenceGenerator(name = "cuisine_seq_generator", allocationSize = 1, sequenceName = "cuisine_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuisine_id")
    private List<RestaurantEntity> restaurantEntityList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CuisineEntity that = (CuisineEntity) o;

        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
