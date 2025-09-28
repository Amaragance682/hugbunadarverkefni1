package com.hugbo.clock_in.domain.entity;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "shift_change_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftChangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    public Shift shift;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(columnDefinition = "text")
    public String reason;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    public JsonNode requestedChanges;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    public Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    public User reviewedBy;

    public Instant reviewedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant created;

    @UpdateTimestamp
    @Column(nullable = false)
    public Instant updated;
}
