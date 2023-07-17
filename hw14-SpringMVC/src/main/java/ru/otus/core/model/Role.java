package ru.otus.core.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority, Persistable {
    @Column(name = "authority")
    String authority;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    public Role(){}

    public Role(String authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public void setAuthority(String authority){
        this.authority = authority;
    }

    public void setId(long id){
        this.id = id;
    }
}
