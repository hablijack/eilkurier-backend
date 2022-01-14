package de.hablijack.eilkurier.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.microprofile.graphql.Description;
import org.jboss.logging.Logger;

@Entity
@Table(name = "subscription", schema = "eilkurier")
@org.eclipse.microprofile.graphql.Type
@Description("Users subscribe to feeds")
public class Subscription extends PanacheEntity {

  private static final Logger LOGGER = Logger.getLogger(Subscription.class.getName());

  @ManyToOne(fetch = FetchType.LAZY)
  public User user;

  @ManyToOne(fetch = FetchType.LAZY)
  public Feed feed;

  @Column(name = "sortindex")
  public int sortindex;

  public void persistIfNotExist() {
    if (find("user = ?1 AND feed = ?2", user, feed).count() == 0) {
      LOGGER.info("New subscription! Storing in DB ...");
      this.persist();
    } else {
      LOGGER.info("subscription already exists, nothing to do ...");
    }
  }
}
