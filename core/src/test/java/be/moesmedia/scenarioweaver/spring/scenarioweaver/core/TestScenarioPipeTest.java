package be.moesmedia.scenarioweaver.spring.scenarioweaver.core;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TestScenarioPipeTest {

    @Test
    void ofStoresValue() {
        final var pipe = TestScenarioPipe.of("hello");
        assertEquals("hello", pipe.value());
    }

    @Test
    void pipeTransformsValue() {
        final var pipe = TestScenarioPipe.of("foo");
        final var intPipe = pipe.pipe(String::length);
        assertEquals(3, intPipe.value());
    }

    @Test
    void pipeCanChainMultipleTimes() {
        final var pipe = TestScenarioPipe.of("foo");
        final var result = pipe
                .pipe(s -> s + "bar")
                .pipe(String::toUpperCase);
        assertEquals("FOOBAR", result.value());
    }

    @Test
    void executeRunsConsumer() {
        final var ref = new AtomicReference<String>();
        TestScenarioPipe.of("baz").execute(ref::set);
        assertEquals("baz", ref.get());
    }
}