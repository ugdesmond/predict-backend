package com.example.demo.model;

import com.example.demo.utils.Constant;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="sickness")
public class Sickness implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(unique = true)
    private String sicknessName;
    private String sicknessDescription;
    @ManyToOne(targetEntity = User.class)
    private User user;
    private Date dateTimeCreated;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSicknessName() {
        return sicknessName;
    }

    public void setSicknessName(String sicknessName) {
        this.sicknessName = sicknessName;
    }

    public String getSicknessDescription() {
        return sicknessDescription;
    }

    public void setSicknessDescription(String sicknessDescription) {
        this.sicknessDescription = sicknessDescription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
