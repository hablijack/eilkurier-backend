package de.hablijack.eilkurier.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "user", schema = "eilkurier")
public class User extends PanacheEntity {

  @Column(name = "nickname", nullable = false)
  public String nickname;

  @Column(name = "email", nullable = false)
  public String email;

  @Column(name = "picture_url", nullable = false)
  @JsonAlias({"picture"})
  public String pictureUrl;

  @Column(name = "wizard_completed", columnDefinition = "BOOLEAN DEFAULT false")
  public Boolean wizardCompleted = false;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  public Set<Subscription> subscriptions;

  public static Optional<User> findByEmailOptional(String email) {
    return find("email = ?1", email).firstResultOptional();
  }
}
