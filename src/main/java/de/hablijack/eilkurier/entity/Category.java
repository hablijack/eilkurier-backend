package de.hablijack.eilkurier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category", schema = "eilkurier")
public class Category extends PanacheEntity {

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY)
    public Set<Feed> feeds;

    @Column(name = "description", nullable = false)
    public String description;

    @Column(name = "name", nullable = false)
    public String name;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
}
