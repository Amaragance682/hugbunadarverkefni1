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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    public Company company;

    @ManyToOne
    @JoinColumn(name = "location_id")
    public Location location;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    public String name;

    @Column(columnDefinition = "text")
    public String description;

    @Builder.Default
    @Column(nullable = false)
    public Boolean isFinished = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false,
    columnDefinition = "timestamptz default current_timestamp")
    public Instant created;

    @UpdateTimestamp
    @Column(nullable = false,
    columnDefinition = "timestamptz default current_timestamp")
    public Instant updated;

    @OneToMany(mappedBy = "task")
    public List<ShiftTask> shiftTasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task other)) return false;
        return Objects.equals(this.id, other.id);
    }
}
