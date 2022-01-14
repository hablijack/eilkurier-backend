package de.hablijack.eilkurier.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.microprofile.graphql.Description;

@Entity
@Table(name = "subscription", schema = "eilkurier")
@org.eclipse.microprofile.graphql.Type
@Description("Users subscribe to feeds")
public class Subscription extends PanacheEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  public User user;

  @ManyToOne(fetch = FetchType.LAZY)
  public Feed feed;

  @Column(name = "sortindex")
  public int sortindex;

}
