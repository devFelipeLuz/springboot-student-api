package br.com.backend.dto.response;

public record DashboardResponse(
        long students,
        long professors,
        long classrooms,
        long subjects,
        long enrollments,
        long assessments
) {
}
