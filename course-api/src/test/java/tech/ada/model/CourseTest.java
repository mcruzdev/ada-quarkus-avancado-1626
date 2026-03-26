package tech.ada.model;

import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void test_equals_and_hashcode() {
        Course course = new Course("Quarkus");
        course.id = 1L;

        Course course1 = new Course("Quarkus");
        course1.id = 1L;
//        Course course1 = course;

        System.out.println("course.equals(course1) = " + course.equals(course1));

    }
}