package dev.matheuscruz.presentation.api;

import dev.matheuscruz.domain.exception.EntityNotFoundException;
import dev.matheuscruz.domain.exception.InsufficientBalanceException;
import dev.matheuscruz.domain.student.StudentService;
import io.quarkus.logging.Log;
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
    public Response transfer(StudentResource.TransferBetweenStudentsRequest request) throws Exception {
        throw new Exception("hello figura");
//        this.studentService.transferCoins(request.from(), request.to(), request.amount());
//        return Response.ok().build();
    }

    @POST
    @Path("/bonifications")
    public Response addBonus(StudentResource.AddBonusRequest request) {
        Log.info("Request to add bonification received: " + request);
        this.studentService.addBonus(request.email());
        return Response.ok().build();
    }
}