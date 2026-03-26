package tech.ada.service;
//
//import io.micrometer.core.annotation.Timed;
//import io.micrometer.core.instrument.Counter;
//import io.micrometer.core.instrument.MeterRegistry;
//import io.micrometer.core.instrument.Timer;
//import io.opentelemetry.api.metrics.MeterBuilder;
//import io.opentelemetry.api.trace.SpanKind;
//import io.opentelemetry.instrumentation.annotations.WithSpan;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import tech.ada.CustomKey;
import tech.ada.CustomKeyGenerator;
import tech.ada.model.Course;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class CourseService {

//    @Inject
    final MeterRegistry meterRegistry;
    final CacheManager cacheManager;

    public CourseService(MeterRegistry meterRegistry, CacheManager cacheManager) {
        this.meterRegistry = meterRegistry;
        this.cacheManager = cacheManager;
    }

    @Transactional
    @Timed(value = "create.course.timed", description = "Time taken to create a course")
    @WithSpan(value = "createCourseNoService", kind = SpanKind.INTERNAL)
//    @io.quarkus.cache.CacheInvalidate(cacheName = "list-all")
//    @CacheInvalidateAll(cacheName = "list-all")
    // Quando o metodo anotado com CacheInvalidate ou CacheResult
    // E esse metodo tem exatamente um argumento esse argumento vai ser a chave do cache
//    @CacheInvalidate(cacheName = "list-all", keyGenerator = CustomKeyGenerator.class)

    @CustomKey("listaDeCursos")
    @CacheInvalidate(cacheName = "list-all", keyGenerator = CustomKeyGenerator.class)
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


    // Quando eu anoto com @CacheResult
    // E o metodo nao tem argumentos listAll() list-all como chave do cache
    // list-all

    @CustomKey("listaDeCursos")
    @CacheResult(cacheName = "list-all", keyGenerator = CustomKeyGenerator.class)
    public List<Course> listAll() {
        Log.info("Listing all courses");
        return Course.listAll();
    }

    public String roles(String usedForKey) {
        Log.info("Listing all roles: " + usedForKey);

        Optional<Cache> listAll = cacheManager.getCache("list-all");

        if (listAll.isPresent()) { // Está presente

            Cache listAllBox = listAll.get(); // Cache

            String result = listAllBox.get("roles", // vai retornar do cache
                    s -> {
                return Set.of("admin", "user").toString(); // vai retornar o que eu colocar aqui
                        // e o que eu colocar aqui vai para o cache
            })
                    .await() // blocking
                    .indefinitely(); // indefinidamente

            Log.info("Roles listed: " + result);

            return result;

        }

        return "";

    }
}
