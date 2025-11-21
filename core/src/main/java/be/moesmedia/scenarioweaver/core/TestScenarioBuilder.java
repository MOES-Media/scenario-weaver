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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class TestScenarioBuilder<TPayload, TContext extends TestScenarioContext<TPayload>> {
    private ActionProvider<TPayload, TContext> actionProvider;
    private StubsProvider<TContext> stubsProvider = ctx -> ctx;
    private PayloadProvider<TContext> payloadProvider = ctx -> ctx;
    private PropertiesProvider<TContext> propertiesProvider = ctx -> ctx;
    private List<AssertionsProvider<TContext>> assertionsProviders;
    private TContext context;
    private String description;

    public static <TPayload, TContext extends TestScenarioContext<TPayload>>
            TestScenarioBuilder<TPayload, TContext> builder() {
        return new TestScenarioBuilder<>();
    }

    public static <TPayload, TContext extends TestScenarioContext<TPayload>>
            TestScenarioBuilder<TPayload, TContext> withAction(ActionProvider<TPayload, TContext> provider) {
        final var testScenarioBuilder = new TestScenarioBuilder<TPayload, TContext>();
        testScenarioBuilder.actionProvider(provider);
        return testScenarioBuilder;
    }

    public TestScenarioBuilder<TPayload, TContext> actionProvider(ActionProvider<TPayload, TContext> actions) {
        this.actionProvider = actions;
        return this;
    }

    public TestScenarioBuilder<TPayload, TContext> context(TContext ctx) {
        this.context = ctx;
        return this;
    }

    public TestScenarioBuilder<TPayload, TContext> stubs(StubsProvider<TContext> provider) {
        this.stubsProvider = provider;
        return this;
    }

    public TestScenarioBuilder<TPayload, TContext> description(String descr) {
        this.description = descr;
        return this;
    }

    public TestScenarioBuilder<TPayload, TContext> payloadProvider(PayloadProvider<TContext> provider) {
        this.payloadProvider = provider;
        return this;
    }

    public TestScenarioBuilder<TPayload, TContext> propertiesProvider(PropertiesProvider<TContext> provider) {
        this.propertiesProvider = provider;
        return this;
    }

    public TestScenarioBuilder<TPayload, TContext> assertionsProviders(List<AssertionsProvider<TContext>> providers) {
        this.assertionsProviders = Objects.isNull(providers) ? null : new ArrayList<>(providers);
        return this;
    }

    public TestScenario<TPayload, TContext> build() {
        if (Objects.isNull(actionProvider)) {
            throw new IllegalStateException("Cannot build TestScenario without action provider");
        }
        if (Objects.isNull(assertionsProviders) || assertionsProviders.isEmpty()) {
            throw new IllegalStateException("Cannot build TestScenario without assertions");
        }
        return new TestScenario<TPayload, TContext>() {
            @Override
            public String description() {
                return description;
            }

            @Override
            public PayloadProvider<TContext> payloadProvider() {
                return payloadProvider;
            }

            @Override
            public PropertiesProvider<TContext> propertiesProvider() {
                return propertiesProvider;
            }

            @Override
            public StubsProvider<TContext> stubs() {
                return stubsProvider;
            }

            @Override
            public List<AssertionsProvider<TContext>> assertions() {
                return Optional.ofNullable(assertionsProviders)
                        .map(Collections::unmodifiableList)
                        .orElse(Collections.emptyList());
            }

            @Override
            public ActionProvider<TPayload, TContext> actionProvider() {
                return actionProvider;
            }

            @Override
            public TContext context() {
                return context;
            }
        };
    }
}
