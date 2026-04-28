package org.synthcity.modulo_4;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

abstract class JavaFxTestBase {

    private static final AtomicBoolean iniciado = new AtomicBoolean(false);

    @BeforeAll
    static void iniciarJavaFx() throws Exception {
        if (iniciado.compareAndSet(false, true)) {
            CountDownLatch latch = new CountDownLatch(1);

            Platform.startup(() -> {
                Platform.setImplicitExit(false);
                latch.countDown();
            });

            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timeout iniciando JavaFX.");
            }
        }
    }

    protected void ejecutarEnFxAndWait(Runnable accion) {
        if (Platform.isFxApplicationThread()) {
            accion.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                accion.run();
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });

        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Timeout ejecutando acción JavaFX.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        if (error.get() != null) {
            throw new RuntimeException(error.get());
        }
    }
}