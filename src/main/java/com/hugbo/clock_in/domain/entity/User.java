package com.hugbo.clock_in.domain.entity;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    @Size(max = 128)
    @Column(nullable = false, length = 128)
    public String name;

    @NotBlank
    @Email
    @Size(max = 128)
    @Column(nullable = false, unique = true, length = 128)
    public String email;

    @NotBlank
    @Size(max = 256)
    @Column(nullable = false, length = 256)
    public String password;

    @Column(nullable = false)
    @Builder.Default
    public Boolean admin = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant created;

    @UpdateTimestamp
    @Column(nullable = false)
    public Instant updated;

    // TODO! add default empty list for all OneToMany relations
    @OneToMany(mappedBy = "user")
    public List<Contract> contracts;

    @OneToMany(mappedBy = "createdBy")
    public List<ShiftNote> shiftNotes;

    @OneToMany(mappedBy = "user")
    public List<EditRequest> editRequests;

    @OneToMany(mappedBy = "reviewedBy")
    public List<EditRequest> reviewedEditRequests;

    @OneToMany(mappedBy = "actor")
    public List<AuditLog> auditLogs;

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public boolean getAdmin() {
        return admin;
    }
    public List<Contract> getContracts() {
        return contracts;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return Objects.equals(this.id, other.id);
    }
}
