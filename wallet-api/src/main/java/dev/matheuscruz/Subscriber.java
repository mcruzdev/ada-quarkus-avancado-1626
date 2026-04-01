package dev.matheuscruz;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class Subscriber {

    @Incoming("finished-lesson-wallet-api")
    public void consume(FinishedLessonEvent finishedLessonEvent) {
        System.out.println("Received finished lesson event: " + finishedLessonEvent);
    }

    public record FinishedLessonEvent(String studentEmail, Integer lessonId) {}
}
