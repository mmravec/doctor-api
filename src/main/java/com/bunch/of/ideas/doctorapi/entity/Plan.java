package com.bunch.of.ideas.doctorapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "Plan")
public class Plan {

    @Id
    private String email;
    private Integer requests;
    private String planName;
    private boolean isFree;
    private String productId;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime nextRequestUpdate;

    public Plan() {
    }

    public Plan(String email, Integer requests, String planName, boolean isFree, String productId, LocalDateTime createDate, LocalDateTime updateDate, LocalDateTime nextRequestUpdate) {
        this.email = email;
        this.requests = requests;
        this.planName = planName;
        this.isFree = isFree;
        this.productId = productId;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.nextRequestUpdate =nextRequestUpdate;
    }

    public Integer getRequests() {
        return requests;
    }

    public void setRequests(Integer requests) {
        this.requests = requests;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public LocalDateTime getNextRequestUpdate() {
        return nextRequestUpdate;
    }

    public void setNextRequestUpdate(LocalDateTime nextRequestUpdate) {
        this.nextRequestUpdate = nextRequestUpdate;
    }
}
