package com.hugbo.clock_in.domain.entity;

import java.time.Instant;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shift_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public Shift shift;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    public String note;

    @ManyToOne
    @JoinColumn(name = "created_by")
    public User createdBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
    columnDefinition = "timestamptz default current_timestamp")
    public Instant created;

    @UpdateTimestamp
    @Column(nullable = false,
    columnDefinition = "timestamptz default current_timestamp")
    public Instant updated;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftNote other)) return false;
        return Objects.equals(this.id, other.id);
    }
}
