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

/**
 * Builds {@link TestScenario} instances using a fluent API.
 * <p>
 * Example usage:
 * <pre>
 * TestScenarioBuilder.builder()
 *     .actionProvider(...)
 *     .assertionsProviders(...)
 *     .build();
 * </pre>
 *
 * @param <TPayload> the type of the payload used in the scenario
 * @param <TContext> the type of the scenario context, extending {@link TestScenarioContext}
 */
public final class TestScenarioBuilder<TPayload, TContext extends TestScenarioContext<TPayload>> {
    private ActionProvider<TPayload, TContext> actionProvider;
    private StubsProvider<TContext> stubsProvider = ctx -> ctx;
    private PayloadProvider<TContext> payloadProvider = ctx -> ctx;
    private ContextProvider<TContext> contextProvider = ctx -> ctx;
    private List<AssertionsProvider<TContext>> assertionsProviders;
    private TContext context;
    private String description;

    /**
     * Constructs a new {@code TestScenarioBuilder} instance.
     */
    private TestScenarioBuilder() {}

    /**
     * Creates a new {@link TestScenarioBuilder} instance.
     * <p>
     * Use this method to start building a test scenario fluently.
     * </p>
     *
     * @param <TPayload> the type of the payload
     * @param <TContext> the type of the test scenario context
     * @return a new {@code TestScenarioBuilder} instance
     */
    public static <TPayload, TContext extends TestScenarioContext<TPayload>>
            TestScenarioBuilder<TPayload, TContext> builder() {
        return new TestScenarioBuilder<>();
    }

    /**
     * Creates a new {@link TestScenarioBuilder} instance with the given {@link ActionProvider}.
     * <p>
     * Use this method to start building a test scenario with an initial action provider.
     * </p>
     *
     * @param provider the action provider to use for the scenario
     * @param <TPayload> the type of the payload
     * @param <TContext> the type of the test scenario context
     * @return a new {@code TestScenarioBuilder} instance with the action provider set
     */
    public static <TPayload, TContext extends TestScenarioContext<TPayload>>
            TestScenarioBuilder<TPayload, TContext> withAction(ActionProvider<TPayload, TContext> provider) {
        final var testScenarioBuilder = new TestScenarioBuilder<TPayload, TContext>();
        testScenarioBuilder.actionProvider(provider);
        return testScenarioBuilder;
    }

    /**
     * Sets the {@link ActionProvider} for this scenario.
     *
     * @param provider the action provider to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> actionProvider(ActionProvider<TPayload, TContext> provider) {
        this.actionProvider = provider;
        return this;
    }

    /**
     * Sets the {@link TestScenarioContext} for this scenario.
     *
     * @param ctx the context to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> context(TContext ctx) {
        this.context = ctx;
        return this;
    }

    /**
     * Sets the {@link StubsProvider} for this scenario.
     *
     * @param provider the stubs provider to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> stubs(StubsProvider<TContext> provider) {
        this.stubsProvider = provider;
        return this;
    }

    /**
     * Sets the description for this scenario.
     *
     * @param descr the description to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> description(String descr) {
        this.description = descr;
        return this;
    }

    /**
     * Sets the {@link PayloadProvider} for this scenario.
     *
     * @param provider the payload provider to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> payloadProvider(PayloadProvider<TContext> provider) {
        this.payloadProvider = provider;
        return this;
    }

    /**
     * Sets the {@link ContextProvider} for this scenario.
     *
     * @param provider the properties provider to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> propertiesProvider(ContextProvider<TContext> provider) {
        this.contextProvider = provider;
        return this;
    }

    /**
     * Sets the {@link AssertionsProvider} for this scenario.
     *
     * @param providers the assertion providers to use
     * @return this builder instance for chaining
     */
    public TestScenarioBuilder<TPayload, TContext> assertionsProviders(List<AssertionsProvider<TContext>> providers) {
        this.assertionsProviders = Objects.isNull(providers) ? null : new ArrayList<>(providers);
        return this;
    }

    /**
     * Builds the {@link TestScenario} instance.
     *
     * @return the TestScenario build with the builder's properties
     */
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
            public ContextProvider<TContext> contextProvider() {
                return contextProvider;
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
