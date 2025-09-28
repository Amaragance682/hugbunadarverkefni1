package com.hugbo.clock_in.domain.entity;

import java.time.Instant;
import java.time.Duration;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "shift_breaks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftBreak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    public Shift shift;

    @NotBlank
    @Column(nullable = false, columnDefinition = "text")
    public String breakType;

    @NotNull
    @Column(nullable = false)
    public Instant startTs;

    public Instant endTs;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant created;

    @UpdateTimestamp
    @Column(nullable = false)
    public Instant updated;
}
