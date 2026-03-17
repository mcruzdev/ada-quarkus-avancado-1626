package dev.matheuscruz.presentation.api;

import dev.matheuscruz.domain.student.Student;
import dev.matheuscruz.domain.student.StudentService;
import io.quarkus.logging.Log;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;
import java.net.URI;

@Path("/students")
public class StudentResource {


    final StudentService studentService;

    public StudentResource(StudentService studentService) {
        this.studentService = studentService;
    }

    @POST
    public Response createStudent(
            CreateStudentRequest request
    ) {
        Student student = new Student(request.name(), request.email());
        Student createdStudent = this.studentService.createStudent(student);
        return Response.created(URI.create("/students/" + createdStudent.getId())).build();
    }

    @GET
    @Path("/{email}")
    public Response getByEmail(@PathParam("email") String email) {

        Log.info("Received email: " + email);

        return Response.ok(
                new GetStudentResponse(1L, "figura@email.com")
        ).build();
    }

    @GET
    public Response listAll() {
        return Response.ok(this.studentService.listAll()).build();
    }

    public record CreateStudentRequest(
            String name,
            String email
    ) {
    }

    public record TransferBetweenStudentsRequest(
            String from,
            String to,
            BigDecimal amount
    ) {
    }

    public record AddBonusRequest(String email) {
    }

    public record GetStudentResponse(Long id, String email) {}
}
