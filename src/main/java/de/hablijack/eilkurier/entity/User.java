package de.hablijack.eilkurier.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "eilkurier")
public class User extends PanacheEntity {

  @Column(name = "nickname", nullable = false)
  public String nickname;

  @Column(name = "email", nullable = false)
  public String email;

  @Column(name = "picture_url", nullable = false)
  public String pictureUrl;

  public static Optional<User> findByEmailOptional(String email) {
    return find("email = ?1", email).firstResultOptional();
  }
}