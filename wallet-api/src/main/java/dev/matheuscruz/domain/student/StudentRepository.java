package dev.matheuscruz.domain.student;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StudentRepository implements PanacheRepositoryBase<Student, String> {
}
