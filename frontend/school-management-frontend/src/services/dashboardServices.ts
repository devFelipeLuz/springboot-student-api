export async function getDashboardCounts() {
    const token = localStorage.getItem("accessToken");

    const headers = {
        Authorization: `Bearer ${token}`
    };

    const responses = Promise.all([
        fetch("https://localhost:8080/assessments", {headers}),
        fetch("https://localhost:8080/attendance", {headers}),
        fetch("https://localhost:8080/classrooms", {headers}),
        fetch("https://localhost:8080/enrollments", {headers}),
        fetch("https://localhost:8080/professors", {headers}),
        fetch("https://localhost:8080/school-years", {headers}),
        fetch("https://localhost:8080/students", {headers}),
        fetch("https://localhost:8080/grades", {headers}),
        fetch("https://localhost:8080/subjects", {headers}),
        fetch("https://localhost:8080/assignments", {headers}),
        fetch("https://localhost:8080/admin/users", {headers})
    ]);

    const data = await Promise.all(
        responses.map(response => response.json)
    )
}