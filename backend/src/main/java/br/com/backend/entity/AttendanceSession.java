package br.com.backend.entity;

import br.com.backend.entity.enums.AttendanceStatus;
import br.com.backend.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "attendance_session",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"teaching_assignment_id", "date"})
    }
)
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assignment_id", nullable = false)
    private TeachingAssignment teachingAssignment;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "attendanceSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttendanceRecord> records = new HashSet<>();

    public AttendanceSession(TeachingAssignment teachingAssignment, LocalDate date) {
        this.teachingAssignment = Objects.requireNonNull(
                teachingAssignment, "Teaching assignment cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
    }

    public void registerAttendance(Enrollment enrollment, AttendanceStatus status) {
        AttendanceRecord record = new AttendanceRecord(this, enrollment, status);
        records.add(record);
    }

    public void updateAttendance(UUID id, AttendanceStatus newStatus) {
        this.records.stream()
                .filter(record -> record.getId().equals(id))
                .findFirst()
                .ifPresentOrElse(record -> record.updateStatus(newStatus),
                        () -> {
                            throw new EntityNotFoundException("Registro de presença não encontrado nesta sessão");
                        }
                );
    }
}
