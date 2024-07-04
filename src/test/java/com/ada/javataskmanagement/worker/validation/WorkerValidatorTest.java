package com.ada.javataskmanagement.worker.validation;

import com.ada.javataskmanagement.worker.model.Worker;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class WorkerValidatorTest {

    private WorkerValidator workerValidator;

    @BeforeEach
    void setUp() {
        workerValidator = WorkerValidator.link(
                new FirstnameWorkerValidator(),
                new LastnameWorkerValidator(),
                new EmailWorkerValidator()
        );
    }

    @Test
    void shouldFailWhenFirstnameIsNull() {
        Worker worker = new Worker();
        worker.setFirstname(null);

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenFirstnameIsEmpty() {
        Worker worker = new Worker();
        worker.setFirstname("");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenFirstnameIsBlank() {
        Worker worker = new Worker();
        worker.setFirstname(" ");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenFirstnameHasInvalidFormat() {
        Worker worker = new Worker();
        worker.setFirstname("Marek$");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenLastnameIsNull() {
        Worker worker = new Worker();
        worker.setLastname(null);

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenLastnameIsEmpty() {
        Worker worker = new Worker();
        worker.setLastname("");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenLastnameIsBlank() {
        Worker worker = new Worker();
        worker.setLastname(" ");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenLastnameHasInvalidFormat() {
        Worker worker = new Worker();
        worker.setLastname("Marek$");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenEmailIsNull() {
        Worker worker = new Worker();
        worker.setEmail(null);

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenEmailIsEmpty() {
        Worker worker = new Worker();
        worker.setEmail("");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenEmailIsBlank() {
        Worker worker = new Worker();
        worker.setEmail(" ");

        assertFalse(workerValidator.check(worker));
    }

    @Test
    void shouldFailWhenEmailHasInvalidFormat() {
        Worker worker = new Worker();
        worker.setEmail("marek@m");

        assertFalse(workerValidator.check(worker));
    }
}
