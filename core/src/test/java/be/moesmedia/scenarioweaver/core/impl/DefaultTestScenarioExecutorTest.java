/*
 * scenario-weaver-core - Scenario based testing
 * Copyright © 2025 MOES-Media (info@moes-media.be)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package be.moesmedia.scenarioweaver.core.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import be.moesmedia.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.core.AssertionsProvider;
import be.moesmedia.scenarioweaver.core.ContextProvider;
import be.moesmedia.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultTestScenarioExecutorTest {

    private DefaultTestScenarioExecutor executor;

    @BeforeEach
    void setUp() {
        executor = new DefaultTestScenarioExecutor();
    }

    static class DummyContext implements TestScenarioContext<String> {
        private String payload;

        public DummyContext() {}

        public DummyContext(String payload) {
            this.payload = payload;
        }

        @Override
        public String payload() {
            return payload;
        }
    }

    @Test
    void shouldThrowExceptionWhenTestScenarioIsNull() {
        assertThrows(IllegalArgumentException.class, () -> executor.execute(null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldExecuteScenarioWithAllProviders() throws Exception {
        final TestScenario<String, DummyContext> scenario = mock(TestScenario.class);
        final PayloadProvider<DummyContext> payloadProvider = mock(PayloadProvider.class);
        final ContextProvider<DummyContext> contextProvider = mock(ContextProvider.class);
        final StubsProvider<DummyContext> stubsProvider = mock(StubsProvider.class);
        final ActionProvider<String, DummyContext> actionProvider = mock(ActionProvider.class);
        final AssertionsProvider<DummyContext> assertion = mock(AssertionsProvider.class);

        DummyContext payloadCtx = new DummyContext("payload");
        DummyContext propertiesCtx = new DummyContext("props");
        DummyContext stubsCtx = new DummyContext("stubs");
        DummyContext resultCtx = new DummyContext("result");

        when(scenario.contextClass()).thenReturn(DummyContext.class);
        when(scenario.payloadProvider()).thenReturn(payloadProvider);
        when(payloadProvider.create(any())).thenReturn(payloadCtx);

        when(scenario.contextProvider()).thenReturn(contextProvider);
        when(contextProvider.create(payloadCtx)).thenReturn(propertiesCtx);

        when(scenario.stubs()).thenReturn(stubsProvider);
        when(stubsProvider.create(propertiesCtx)).thenReturn(stubsCtx);

        when(scenario.actionProvider()).thenReturn(actionProvider);
        when(actionProvider.execute(stubsCtx.payload(), stubsCtx)).thenReturn(resultCtx);

        when(scenario.assertions()).thenReturn(List.of(assertion));

        executor.execute(scenario);

        verify(payloadProvider).create(any());
        verify(contextProvider).create(payloadCtx);
        verify(stubsProvider).create(propertiesCtx);
        verify(actionProvider).execute(stubsCtx.payload(), stubsCtx);
        verify(assertion).execute(resultCtx);
    }
}
