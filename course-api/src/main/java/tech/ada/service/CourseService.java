package tech.ada.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import tech.ada.model.Course;

@ApplicationScoped
public class CourseService {

    final MeterRegistry meterRegistry;

    public CourseService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    @Timed(value = "create.course.timed", description = "Time taken to create a course")
    @WithSpan(value = "createCourseNoService", kind = SpanKind.INTERNAL)
    public Course createCourse(Course course) {

        Timer.builder("create.course.timer").withRegistry(meterRegistry).withTag("class", "1626")
                .record(() -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        if (course.getName().toLowerCase().contains("quarkus")) {
            Log.info("Creating course with quarkus");

            Counter.builder("course.with.quarkus")
                    .tag("class", "1626")
                    .register(meterRegistry)
                    .increment();


            meterRegistry.counter("course.with.quarkus").increment();
        }

        course.persist();
        return course;
    }
}
