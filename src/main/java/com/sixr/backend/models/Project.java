package com.sixr.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="projects")
public class Project extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long projectid;

    private String  name,
                    description;

    private long amount;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer","projects"})
    private User owner;

    private String  email,
                    phone,
                    status;

    public Project(){
    }

    public Project(String name, String description, long amount, User user, String email, String phone, String status) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.owner = user;
        this.email = email;
        this.phone = phone;
        this.status=status;
    }

    public long getProjectid() {
        return projectid;
    }

    public void setProjectid(long projectid) {
        this.projectid = projectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return getProjectid() == project.getProjectid() &&
                getAmount() == project.getAmount() &&
                getName().equals(project.getName()) &&
                getDescription().equals(project.getDescription()) &&
                getOwner().equals(project.getOwner()) &&
                Objects.equals(getEmail(), project.getEmail()) &&
                Objects.equals(getPhone(), project.getPhone()) &&
                Objects.equals(getStatus(), project.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProjectid(), getName(), getDescription(), getAmount(), getOwner(), getEmail(), getPhone(), getStatus());
    }
}
