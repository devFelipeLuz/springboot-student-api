package br.com.backend.builders.entity;

import br.com.backend.entity.AttendanceRecord;
import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.Enrollment;
import br.com.backend.entity.enums.AttendanceStatus;

public class AttendanceRecordBuilder {

    private AttendanceSession session = AttendanceSessionBuilder.builder().build();
    private Enrollment enrollment = EnrollmentBuilder.builder().build();
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    public static AttendanceRecordBuilder builder() {
        return new AttendanceRecordBuilder();
    }

    public AttendanceRecordBuilder withSession(AttendanceSession session) {
        this.session = session;
        return this;
    }

    public AttendanceRecordBuilder withEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
        return this;
    }

    public AttendanceRecordBuilder withStatus(AttendanceStatus status) {
        this.status = status;
        return this;
    }

    public AttendanceRecord build() {
        return new AttendanceRecord(session, enrollment, status);
    }
}
