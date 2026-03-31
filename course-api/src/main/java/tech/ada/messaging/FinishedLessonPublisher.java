package tech.ada.messaging;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import tech.ada.dto.FinishedLessonMessage;

import java.time.Duration;
import java.util.Random;

@ApplicationScoped
public class FinishedLessonPublisher {

    @Channel("finished-lessons")
    Emitter<FinishedLessonMessage> emitter;

    final Random random = new Random();

    public void emitFinishedLessonMessage(FinishedLessonMessage finishedLessonMessage) {
        emitter.send(finishedLessonMessage);
    }

    @Outgoing("finished-lessons")
    public Multi<Message<FinishedLessonMessage>> publishFinishedLesson() {
        return Multi.createFrom()
                .ticks()
                .every(Duration.ofSeconds(1))
                .map(aLong -> {
                            Log.info("Publishing event at: " + aLong);
                            FinishedLessonMessage payload = new FinishedLessonMessage(
                                    "outgoing@email.com",
                                    random.nextInt()
                            );
                            return Message.of(payload);
                        }
                );
    }
}
