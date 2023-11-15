package de.hablijack.eilkurier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

  public Category persistIfNotExist() {
    if (find("name = ?1", name).count() == 0) {
      this.persist();
      return this;
    } else {
      return (Category) find("name = ?1", name).list().get(0);
    }
  }
}
