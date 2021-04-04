package com.iorazutkin.graduatework.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.user.UserView;
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
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(UserView.class)
  private Long id;

  @Column
  @JsonView(UserView.class)
  private String fullName;

  @Column
  private String username;

  @Column
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  @JsonView(UserView.class)
  private Role role;

  public Boolean isStudent () {
    return this.getRole().getName().equals("STUDENT");
  }

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
