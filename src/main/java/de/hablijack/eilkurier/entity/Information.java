package de.hablijack.eilkurier.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;

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
  @Type(type = "org.hibernate.type.TextType")
  public String title;
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
  public String picture;
  @Lob
  @Column(nullable = false)
  @Type(type = "org.hibernate.type.TextType")
  public String message;
  @Lob
  @Column(nullable = false)
  @Type(type = "org.hibernate.type.TextType")
  public String textonlymessage;
  @ManyToOne(fetch = FetchType.LAZY)
  public Feed feed;

  public static boolean existsByTitleAndFeed(String title, Feed feed) {
    return find("title = ?1 and feed = ?2", title, feed).count() > 0;
  }
}
