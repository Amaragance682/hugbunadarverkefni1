package com.hugbo.clock_in.domain.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "companies")
@Data
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 256)
    @Column(nullable = false, length = 256)
    public String name;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant created;

    @UpdateTimestamp
    @Column(nullable = false)
    public Instant updated;

    @OneToMany(mappedBy = "company")
    public List<Contract> contracts;

    @OneToMany(mappedBy = "company")
    public List<Location> locations;

    @OneToMany(mappedBy = "company")
    public List<Task> tasks;

    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Company() {}
}
