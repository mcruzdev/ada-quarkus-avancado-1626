package tech.ada.messaging;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import tech.ada.dto.FinishedLessonMessage;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class FinishedLessonSubscriber {


    @Incoming("finished-lessons-events")
    public CompletionStage<Void> consume(Message<FinishedLessonMessage> message) {

        Log.info("Received message: " + message.getPayload().studentEmail() + " - " + message.getPayload().lessonId());

        return message.ack();
    }

}
