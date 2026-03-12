package dev.matheuscruz.presentation.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class TransferResourceTest {

    private static final Logger log = LoggerFactory.getLogger(TransferResourceTest.class);

    @Test
    void should_detect_lost_update_using_transfer_count() throws Exception {

        int quantityOfTransfers = 20;
        float amount = 10.0f;

        ExecutorService executor = Executors.newFixedThreadPool(30);
        CountDownLatch start = new CountDownLatch(1);

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < quantityOfTransfers; i++) {

            futures.add(executor.submit(() -> {

                await(start);

                return given()
                        .contentType("application/json")
                        .body("""
                        {
                            "from": "alice@email.com",
                            "to": "bob@email.com",
                            "amount": 10.00
                        }
                        """)
                        .when()
                        .post("/transfers")
                        .then()
                        .extract()
                        .statusCode();
            }));
        }

        start.countDown();

        AtomicInteger success = new AtomicInteger();
        for (Future<Integer> f : futures) {
            if (f.get() == 200) {
                success.incrementAndGet();
            }
        }

        float expectedAlice = 100.0f - (success.get() * amount);
        float expectedBob   = 100.0f + (success.get() * amount);

        log.info("Success calls: {}", success.get());

        given()
                .when()
                .get("/students")
                .then()
                .statusCode(200)
                .body("find { it.email == 'alice@email.com' }.quarkusCoins", equalTo(expectedAlice))
                .body("find { it.email == 'bob@email.com' }.quarkusCoins", equalTo(expectedBob));
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}