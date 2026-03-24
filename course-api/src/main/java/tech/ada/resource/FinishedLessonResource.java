package tech.ada.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.RestrictedBindingSource;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import tech.ada.client.WalletApi;
import tech.ada.model.FinishedLesson;

import java.net.URI;
import java.util.Map;

@Authenticated // somente tokens validos
@Path("/finished-lessons")
public class FinishedLessonResource {

    final ObjectMapper mapper;

//    @Inject
//    @RestClient
//    WalletApi walletApi;

    @Inject
    @RestClient
    WalletApi walletApi;

    @ConfigProperty(name = "wallet.api.base.url")
    String walletBaseUrl;

    @ConfigProperty(name = "aws.secret")
    String awsSecret;

    public FinishedLessonResource(ObjectMapper mapper
//                                  , @RestClient  WalletApi walletApi
    ) {
        this.mapper = mapper;
//        this.walletApi = walletApi;
    }

    @POST
    @Transactional
    public Response finishedLesson(FinishedLessonRequest req) {

        FinishedLesson finishedLesson = new FinishedLesson(req.email(), req.lessonId());

        // Validar se existe uma Lesson

        finishedLesson.persist();

        Log.info("Aws Secret is: " + awsSecret);

        var byEmail = walletApi.findByEmail("figura123@email.com");

        WalletApi.GetStudentResponse response = byEmail.readEntity(WalletApi.GetStudentResponse.class);

        Log.info("Response body: " + response);

//        Response walletApiResponse = walletApi.addBonification(
//                new WalletApi.AddBonusRequest(req.email())
//        );

//        Log.info("Status code: " + walletApiResponse.getStatus());

//        try {

//            AddBonusRequest addBonusRequest = new AddBonusRequest(req.email());
//
//            String payload = mapper.writeValueAsString(addBonusRequest);
//            HttpClient client = HttpClient.newBuilder().build();
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(walletBaseUrl + "/transfers/bonifications"))
//                    .POST(HttpRequest.BodyPublishers.ofString(
//                            payload, StandardCharsets.UTF_8
//                    )).header("Content-Type", "application/json")
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            Log.info("Response status: " + response.statusCode());
//            Log.info("Response body: " + response.body());
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); //
//            throw new RuntimeException(e);
//        }

        return Response.created(URI.create("/finished-lessons/" + finishedLesson.id)).build();
    }

    public record FinishedLessonRequest(String email, Long lessonId) {
    }


}
