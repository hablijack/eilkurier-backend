package de.hablijack.eilkurier.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import org.jboss.logging.Logger;

@Entity
@Table(name = "subscription", schema = "eilkurier")
public class Subscription extends PanacheEntity {

  private static final Logger LOGGER = Logger.getLogger(Subscription.class.getName());

  @ManyToOne(fetch = FetchType.LAZY)
  public User user;

  @ManyToOne(fetch = FetchType.LAZY)
  public Feed feed;

  @Column(name = "sortindex")
  public int sortindex;

  public static List<Subscription> findByUser(User christoph) {
    return find("user = ?1", christoph).list();
  }

  public void persistIfNotExist() {
    if (find("user = ?1 AND feed = ?2", user, feed).count() == 0) {
      LOGGER.info("New subscription! Storing in DB ...");
      this.persist();
    } else {
      LOGGER.info("subscription already exists, nothing to do ...");
    }
  }
}
