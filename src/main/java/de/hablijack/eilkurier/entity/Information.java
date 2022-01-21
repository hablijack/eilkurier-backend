package de.hablijack.eilkurier.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.eclipse.microprofile.graphql.Description;
import org.hibernate.annotations.Type;


@Entity
@Table(name = "information", schema = "eilkurier")
@org.eclipse.microprofile.graphql.Type
@Description("The actual news to be consumed")
public class Information extends PanacheEntity {

  @Transient
  @Description("Ranking of the information, how important that news is for you.")
  public final int weight = 0;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @Description("Point in time when this news were produced")
  public Date timestamp;

  @Lob
  @Column(nullable = false)
  @Type(type = "org.hibernate.type.TextType")
  public String title;

  @Column(nullable = false)
  public String guid;

  @Lob
  @Column(nullable = false)
  @Type(type = "org.hibernate.type.TextType")
  public String author;

  @Lob
  @Column(nullable = false)
  @Type(type = "org.hibernate.type.TextType")
  public String link;

  @Lob
  @Column
  @Type(type = "org.hibernate.type.TextType")
  public String pictures;

  @Lob
  @Column(nullable = false)
  @Type(type = "org.hibernate.type.TextType")
  public String textonlymessage;

  @ManyToOne(fetch = FetchType.LAZY)
  public Feed feed;

  public static boolean existsByGuidAndFeed(String guid, Feed feed) {
    return find("guid = ?1 and feed = ?2", guid, feed).count() > 0;
  }

  public static Collection<? extends Information> findByFeed(Feed feed) {
    return find("feed = ?1", feed).list();
  }

  public Set<String> getPictureList() {
    return Set.of(this.pictures.split("\\|\\|"));
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
