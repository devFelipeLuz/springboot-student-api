# School Management API

Backend REST API for managing school operations such as students, enrollments, assessments, attendance and grades.

⚠️ Project currently in development.

## Features

- Student enrollment management
- Teaching assignments
- Assessments and grades
- Attendance tracking
- Authentication with JWT
- REST API documentation with Swagger

## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- JWT Authentication
- Maven
- Swagger / OpenAPI

## Architecture

The project follows a layered architecture:

controller
service
repository
entity
dto
mapper
exception
security

## Domain Model

Core entities:

- Student
- Teacher
- Classroom
- Subject
- TeachingAssignment
- Enrollment
- Assessment
- StudentGrade
- AttendanceSession
- AttendanceRecord
- User
- SchoolYear

## Project Status

The domain model and main services are implemented.  
Controllers and integration testing are still being finalized.

## Author

Felipe Luz
