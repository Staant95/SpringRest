package com.smartshop.models;


import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property  = "id", scope = Shoplist.class)
public class Shoplist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("shoplists")
    private Set<User> users = new HashSet<>();


    @OneToMany(
            mappedBy = "shoplist",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference("shoplistProducts")
    private Set<ProductShoplist> products = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shoplist shoplist = (Shoplist) o;
        return getId().equals(shoplist.getId()) &&
                Objects.equals(getName(), shoplist.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
