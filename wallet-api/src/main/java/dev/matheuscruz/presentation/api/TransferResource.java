package dev.matheuscruz.presentation.api;

import dev.matheuscruz.domain.student.InsufficientBalanceException;
import dev.matheuscruz.domain.student.StudentService;
import dev.matheuscruz.domain.student.UserNotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Path("/transfers")
public class TransferResource {

    private static final Logger log = LoggerFactory.getLogger(TransferResource.class);
    final StudentService studentService;

    public TransferResource(StudentService studentService) {
        this.studentService = studentService;
    }

    @POST
    public Response transfer(StudentResource.TransferBetweenStudentsRequest request) {
        try {
            this.studentService.transferCoins(request.from(), request.to(), request.amount());
        } catch (InsufficientBalanceException e) {
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(Map.of("message", e.getMessage()))
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", e.getMessage()))
                    .build();
        } catch (Exception e) {
            // RuntimeException é uma Exception
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok().build();
    }
}