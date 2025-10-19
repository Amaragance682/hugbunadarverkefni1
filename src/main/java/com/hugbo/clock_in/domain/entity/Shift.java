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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    public Contract contract;

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

    @OneToMany(mappedBy = "shift")
    public List<ShiftTask> shiftTasks;

    @OneToMany(mappedBy = "shift")
    public List<ShiftBreak> shiftBreaks;

    @OneToMany(mappedBy = "shift")
    public List<ShiftNote> shiftNotes;

    @OneToMany(mappedBy = "shift")
    public List<ShiftFlag> shiftFlags;

    @OneToMany(mappedBy = "shift")
    public List<EditRequest> editRequests;
}
