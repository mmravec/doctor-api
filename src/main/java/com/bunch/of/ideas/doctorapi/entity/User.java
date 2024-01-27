package com.bunch.of.ideas.doctorapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
public class User {

    @Id
    private String email;
    private String name;
    private String imageUrl;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private String threadId;

    public User(String email, String name, String imageUrl, LocalDateTime createDate, LocalDateTime updateDate) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.createDate=createDate;
        this.updateDate=updateDate;
    }

    public User() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
}
