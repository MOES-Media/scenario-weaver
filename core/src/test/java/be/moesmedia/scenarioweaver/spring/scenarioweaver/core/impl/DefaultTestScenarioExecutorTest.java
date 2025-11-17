package be.moesmedia.scenarioweaver.spring.scenarioweaver.core.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.AssertionsProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.PropertiesProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioProperties;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultTestScenarioExecutorTest {

    private DefaultTestScenarioExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new DefaultTestScenarioExecutor();
    }

    @Test
    void shouldThrowExceptionWhenTestScenarioIsNull() {
        assertThrows(IllegalArgumentException.class, () -> executor.execute(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldExecuteScenarioWithAllProviders() {
        final var scenario = mock(TestScenario.class);
        final var payloadProvider = mock(PayloadProvider.class);
        final var propertiesProvider = mock(PropertiesProvider.class);
        final var actionProvider = mock(ActionProvider.class);
        final var assertion = mock(AssertionsProvider.class);
        final var stubs = mock(StubsProvider.class);

        final var payload = "payload";
        final var initialProps = mock(TestScenarioProperties.class);
        final var ctxProps = mock(TestScenarioProperties.class);
        final var ctx = "context";
        final var result = 42;

        when(scenario.payloadProvider()).thenReturn(payloadProvider);
        when(payloadProvider.create(null)).thenReturn(payload);

        when(scenario.propertiesProvider()).thenReturn(propertiesProvider);
        when(propertiesProvider.create(null)).thenReturn(initialProps);
        when(propertiesProvider.create(ctx)).thenReturn(ctxProps);

        when(scenario.stubs()).thenReturn(stubs);
        when(stubs.create(payload, initialProps)).thenReturn(ctx);

        when(initialProps.merge(ctxProps)).thenReturn(ctxProps);

        when(scenario.actionProvider()).thenReturn(actionProvider);
        when(actionProvider.execute(payload, ctxProps)).thenReturn(result);

        when(scenario.assertions()).thenReturn(List.of(assertion));

        executor.execute(scenario);

        verify(payloadProvider).create(null);
        verify(propertiesProvider).create(null);
        verify(stubs).create(payload, initialProps);
        verify(propertiesProvider).create(ctx);
        verify(initialProps).merge(ctxProps);
        verify(actionProvider).execute(payload, ctxProps);
        verify(assertion).execute(result, payload, ctx);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldHandleNullStubsAndInitialProps() {
        final var scenario = mock(TestScenario.class);
        final var payloadProvider = mock(PayloadProvider.class);
        final var propertiesProvider = mock(PropertiesProvider.class);
        final var actionProvider = mock(ActionProvider.class);
        final var assertion = mock(AssertionsProvider.class);

        final var payload = "payload";
        final var ctxProps = new TestProps("ctx");
        final var result = 42;

        when(scenario.payloadProvider()).thenReturn(payloadProvider);
        when(payloadProvider.create(null)).thenReturn(payload);

        when(scenario.propertiesProvider()).thenReturn(propertiesProvider);
        when(propertiesProvider.create(null)).thenReturn(null);
        when(propertiesProvider.create(null)).thenReturn(ctxProps);

        when(scenario.stubs()).thenReturn(null);

        when(scenario.actionProvider()).thenReturn(actionProvider);
        when(actionProvider.execute(payload, ctxProps)).thenReturn(result);

        when(scenario.assertions()).thenReturn(List.of(assertion));

        executor.execute(scenario);

        verify(payloadProvider).create(null);
        verify(propertiesProvider, times(2)).create(null);
        verify(actionProvider).execute(payload, ctxProps);
        verify(assertion).execute(result, payload, null);
    }

    record TestProps(String value) implements TestScenarioProperties {

        @Override
        @SuppressWarnings("unchecked")
        public TestProps merge(TestScenarioProperties other) {
            return (other instanceof TestProps) ? (TestProps) other : this;
        }
    }
}
