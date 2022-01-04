package de.hablijack.eilkurier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

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
  public String language;
  public String copyright;

  public Feed() {

  }

  public Feed(String name, String description, String picture, String pictureContentType, String url,
              Category category) {
    this.name = name;
    this.description = description;
    this.picture = picture;
    this.pictureContentType = pictureContentType;
    this.url = url;
    this.category = category;
  }

  public static List<Feed> finByCategoryIds(List<Long> categoryIds) {
    return find("category.id in (?1)", categoryIds).list();
  }

  public void persistIfNotExist() {
    if (find("url = ?1", url).count() == 0) {
      this.persist();
    }
  }

  @Override
  public String toString() {
    return "Feed{"
        + "url='" + url + '\''
        + ", name='" + name + '\''
        + ", description='" + description + '\''
        + ", picture='" + picture + '\''
        + ", pictureContentType='" + pictureContentType + '\''
        + ", category=" + category
        + ", information=" + information
        + ", language='" + language + '\''
        + ", copyright='" + copyright + '\''
        + '}';
  }
}
