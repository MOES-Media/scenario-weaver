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
package be.moesmedia.scenarioweaver.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class TestScenarioBuilderTest {

    static class DummyContext implements TestScenarioContext<String> {
        private String payload = null;

        public DummyContext payload(String payload) {
            this.payload = payload;
            return this;
        }

        @Override
        public String payload() {
            return payload;
        }
    }

    static class DummyActionProvider implements ActionProvider<String, DummyContext> {
        @Override
        public DummyContext execute(String s, DummyContext context) {
            return context.payload(s);
        }
    }

    static class DummyAssertionsProvider implements AssertionsProvider<DummyContext> {
        @Override
        public void execute(DummyContext context) {}
    }

    @Test
    void builderStaticMethodCreatesInstance() {
        final var builder = TestScenarioBuilder.<String, DummyContext>builder();
        assertNotNull(builder);
    }

    @Test
    void withActionStaticMethodSetsActionProvider() {
        final var actionProvider = new DummyActionProvider();
        final var builder = TestScenarioBuilder.withAction(actionProvider);
        assertNotNull(builder);
        builder.assertionsProviders(List.of(new DummyAssertionsProvider()));
        final var scenario = builder.build();
        assertSame(actionProvider, scenario.actionProvider());
    }

    @Test
    void fluentSettersSetFields() {
        final var builder = TestScenarioBuilder.<String, DummyContext>builder();
        final var actionProvider = new DummyActionProvider();
        final var stubsProvider = (StubsProvider<DummyContext>) ctx -> ctx;
        final var payloadProvider = (PayloadProvider<DummyContext>) ctx -> ctx;
        final var propertiesProvider = (PropertiesProvider<DummyContext>) ctx -> ctx;
        final var assertionsProvider = new DummyAssertionsProvider();
        final var context = new DummyContext().payload("ctx");
        final var description = "desc";

        builder.actionProvider(actionProvider)
                .stubs(stubsProvider)
                .payloadProvider(payloadProvider)
                .propertiesProvider(propertiesProvider)
                .assertionsProviders(List.of(assertionsProvider))
                .context(context)
                .description(description);

        final var scenario = builder.build();
        assertSame(actionProvider, scenario.actionProvider());
        assertSame(stubsProvider, scenario.stubs());
        assertSame(payloadProvider, scenario.payloadProvider());
        assertSame(propertiesProvider, scenario.propertiesProvider());
        assertEquals(List.of(assertionsProvider), scenario.assertions());
        assertSame(context, scenario.context());
        assertEquals(description, scenario.description());
    }

    @Test
    void buildThrowsIfNoActionProvider() {
        final var builder = TestScenarioBuilder.<String, DummyContext>builder()
                .assertionsProviders(List.of(new DummyAssertionsProvider()));
        final var ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("action provider"));
    }

    @Test
    void buildThrowsIfNoAssertions() {
        final var builder =
                TestScenarioBuilder.<String, DummyContext>builder().actionProvider(new DummyActionProvider());
        final var ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("assertions"));
    }

    @Test
    void buildThrowsIfNullAssertions() {
        final var builder =
                TestScenarioBuilder.withAction(new DummyActionProvider()).assertionsProviders(null);
        final var ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("assertions"));
    }

    @Test
    void buildThrowsIfAssertionsListIsEmpty() {
        final var builder = TestScenarioBuilder.<String, DummyContext>builder()
                .actionProvider(new DummyActionProvider())
                .assertionsProviders(List.of());
        final var ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("assertions"));
    }

    @Test
    void defaultProvidersAreIdentityFunctions() {
        final var builder = TestScenarioBuilder.<String, DummyContext>builder()
                .actionProvider(new DummyActionProvider())
                .assertionsProviders(List.of(new DummyAssertionsProvider()))
                .context(new DummyContext().payload("default"));

        final var scenario = builder.build();

        final var ctx = new DummyContext().payload("test");

        assertSame(ctx, scenario.stubs().create(ctx));
        assertSame(ctx, scenario.payloadProvider().create(ctx));
        assertSame(ctx, scenario.propertiesProvider().create(ctx));
    }
}
