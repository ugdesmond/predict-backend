package com.example.demo.model;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false,unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String salt;
    @Column(nullable = false)
    String role;
    @Column
    String token;

    public User() {

    }

    public User(String username, String password, String salt, String role) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
}
