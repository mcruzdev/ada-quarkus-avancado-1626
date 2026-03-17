package dev.matheuscruz.domain.student;

import dev.matheuscruz.domain.exception.InsufficientBalanceException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "students")
public class Student {

    private static final int DEFAULT_QUARKUS_COINS = 100;

    @Id
    private String id;
    private String name;
    private String email;
    @Column(name = "quarkus_coins")
    private BigDecimal quarkusCoins;

    // required by JPA
    protected Student() {
    }

    public Student(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.quarkusCoins = BigDecimal.valueOf(DEFAULT_QUARKUS_COINS);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getQuarkusCoins() {
        return quarkusCoins;
    }

    public void transferCoins(Student receiverStudent, BigDecimal amount) {
        BigDecimal currentBalance = this.getQuarkusCoins();

        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "The Student with ID " + this.getId() + " does not have sufficient balance"
            );
        }

        this.quarkusCoins = currentBalance.subtract(amount);
        receiverStudent.quarkusCoins =
                receiverStudent.quarkusCoins.add(amount);
    }

    public void addBonus(BigDecimal bonus) {
        this.quarkusCoins = this.quarkusCoins.add(bonus);
    }
}