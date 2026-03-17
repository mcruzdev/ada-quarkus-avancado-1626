package tech.ada.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class FinishedLesson extends PanacheEntity {

    private String studentEmail;
    private Long lessonId;

    // required by JPA
    protected FinishedLesson() {}

    public FinishedLesson(String studentEmail, Long lessonId) {
        this.lessonId = lessonId;
        this.studentEmail = studentEmail;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public Long getLessonId() {
        return lessonId;
    }
}
