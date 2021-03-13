package com.iorazutkin.graduatework.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Embeddable
@Entity(name = "usr")
public class User implements Serializable, UserDetails {
  public interface Info {}

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({ Info.class})
  private Long id;

  @Column
  @JsonView({ Info.class})
  private String fullName;

  @Column
  @JsonView({ Info.class})
  private String username;

  @Column
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  @JsonView({ Info.class})
  private Role role;

  @Override
  public boolean isAccountNonExpired () {
    return true;
  }

  @Override
  public boolean isAccountNonLocked () {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired () {
    return true;
  }

  @Override
  public boolean isEnabled () {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities () {
    return null;
  }
}
