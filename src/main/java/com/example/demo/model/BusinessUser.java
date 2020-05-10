package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * lahray 06/10/2018 10:26 PM
 */

@Entity
@Table(name = "business_user", uniqueConstraints = @UniqueConstraint(name = "UK_business_and_user", columnNames = {"business_id", "user_id"}))
public class BusinessUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @ManyToOne
//    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_business_user_business_id"))
//    private Business business;
    @ManyToOne(targetEntity = Business.class)
    private Business business;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_business_user_user_id"))
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
