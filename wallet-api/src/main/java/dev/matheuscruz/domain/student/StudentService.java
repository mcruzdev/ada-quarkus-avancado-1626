package dev.matheuscruz.domain.student;

import dev.matheuscruz.IntentionalException;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student createStudent(Student student) {
        studentRepository.persist(student);
        return student;
    }

    public List<Student> listAll() {
        return studentRepository.listAll();
    }

    @Transactional
    public void transferCoins(String from, String to, BigDecimal amount) {

        Student senderStudent = studentRepository.find("email = :email",
                        Parameters.with("email", from))
                .firstResultOptional()
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        Student receiverStudent = studentRepository.find("email = :email", Parameters.with("email", to))
                .firstResultOptional()
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        log.info("Sender has: {}", senderStudent.getQuarkusCoins());
        log.info("Receiver has: {}", receiverStudent.getQuarkusCoins());

        // Force race condition
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        senderStudent.transferCoins(receiverStudent, amount);

        log.info("---------------------------- after ----------------------------");

        log.info("Sender has: {}", senderStudent.getQuarkusCoins());
        log.info("Receiver has: {}", receiverStudent.getQuarkusCoins());
    }
}
