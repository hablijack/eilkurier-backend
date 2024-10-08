package de.hablijack.eilkurier.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "information", schema = "eilkurier")
public class Information extends PanacheEntity {

  @Transient
  public final int weight = 0;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  public Date timestamp;

  @Lob
  @Column(nullable = false)
  public String title;

  @Column(nullable = false)
  public String guid;

  @Lob
  @Column(nullable = false)
  public String author;

  @Lob
  @Column(nullable = false)
  public String link;

  @Lob
  @Column
  @JsonIgnore
  public String pictures;

  @Lob
  @Column(nullable = false)
  public String textonlymessage;

  @ManyToOne(fetch = FetchType.LAZY)
  public Feed feed;

  public static boolean existsByGuidAndFeed(String guid, Feed feed) {
    return find("guid = ?1 and feed = ?2", guid, feed).count() > 0;
  }

  public static Collection<? extends Information> findByFeed(Feed feed) {
    return find("feed = ?1", feed).list();
  }

  public String getShortFormattedTimestamp() {
    String pattern = "dd.MM.";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return simpleDateFormat.format(this.timestamp);
  }

  public Set<String> getAllPicturesAsList() {
    return new HashSet<String>(List.of(this.pictures.split("\\|\\|")));
  }

  public String getHeadlinePicture() {
    return this.getAllPicturesAsList().stream().findFirst().get();
  }

  public Set<String> getRemainingPictureList() {
    Set<String> allPictures = this.getAllPicturesAsList();
    allPictures.remove(this.getHeadlinePicture());
    return allPictures;
  }

  @Override
  public String toString() {
    return "Information{"
        + "weight=" + weight
        + ", timestamp=" + timestamp
        + ", title='" + title + '\''
        + ", guid='" + guid + '\''
        + ", author='" + author + '\''
        + ", link='" + link + '\''
        + ", picture='" + pictures + '\''
        + ", textonlymessage='" + textonlymessage + '\''
        + ", feed=" + feed
        + '}';
  }
}
