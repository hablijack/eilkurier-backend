package de.hablijack.eilkurier.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "information", schema = "eilkurier")
public class Information extends PanacheEntity {

    @Transient
    public final int weight = 0;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date timestamp;
    @Column(nullable = false)
    public String title;
    @Column(nullable = false)
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
