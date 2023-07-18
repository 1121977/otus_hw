package ru.otus.core.model;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

@Embeddable
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Column(nullable = false)
    String authority;

    public Role(){}

    public Role(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }


    public void setAuthority(String authority){
        this.authority = authority;
    }

}
