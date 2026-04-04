package br.com.backend.entity;

import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "attendance_record",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"attendance_session_id", "enrollment_id"})
})
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_session_id", nullable = false)
    private AttendanceSession attendanceSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    public AttendanceRecord(AttendanceSession attendanceSession,
                            Enrollment enrollment,
                            AttendanceStatus status) {

        this.attendanceSession = Objects.requireNonNull(
                attendanceSession, "AttendanceSession cannot be null");
        this.enrollment = Objects.requireNonNull(
                enrollment, "Enrollment cannot be null");
        this.status = ensureStatus(status);
    }

    public void updateStatus(AttendanceStatus status) {
        this.status = ensureStatus(status);
    }

    private AttendanceStatus ensureStatus(AttendanceStatus status) {
        if (status == null) {
            throw new BusinessException("Status cannot be null");
        }

        return status;
    }
}
