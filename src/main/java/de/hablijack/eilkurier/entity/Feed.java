package de.hablijack.eilkurier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "feed", schema = "eilkurier")
public class Feed extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String url;

    @Column(nullable = false)
    public String name;

    public String description;

    public String picture;

    public String pictureContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    public Category category;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY)
    @OrderBy("timestamp DESC")
    public Set<Information> information;

    public Feed() {

    }

    public Feed(String name, String description, String picture, String pictureContentType, String url, Category category) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.pictureContentType = pictureContentType;
        this.url = url;
        this.category = category;
    }
}
