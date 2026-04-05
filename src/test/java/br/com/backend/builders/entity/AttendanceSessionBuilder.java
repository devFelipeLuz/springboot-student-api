package br.com.backend.builders.entity;

import br.com.backend.entity.AttendanceSession;
import br.com.backend.entity.TeachingAssignment;

import java.time.LocalDate;

public class AttendanceSessionBuilder {

    private TeachingAssignment assignment = TeachingAssignmentBuilder.builder().build();
    private LocalDate date = LocalDate.now();

    public static AttendanceSessionBuilder builder() {
        return new AttendanceSessionBuilder();
    }

    public AttendanceSessionBuilder withAssignment(TeachingAssignment assignment) {
        this.assignment = assignment;
        return this;
    }

    public AttendanceSessionBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public AttendanceSession build() {
        return new AttendanceSession(assignment, date);
    }
}
