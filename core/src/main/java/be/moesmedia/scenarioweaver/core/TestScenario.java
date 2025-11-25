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

import java.util.List;

/**
 * Represents a complete setup for an integration test scenario.
 * <p>
 * A test scenario automates the execution of an integration test by composing and configuring
 * specific providers and interfaces. It defines the context, payload, stubs, actions, and assertions
 * required to execute and validate a scenario, enabling repeatable and maintainable integration testing.
 * </p>
 *
 * <p>
 * By implementing and setting the relevant providers, a test scenario manages the initialization,
 * execution, and verification steps, allowing for flexible and modular test definitions.
 * </p>
 *
 * @param <TPayload> the type of payload used in the scenario
 * @param <TContext> the type of scenario context, extending {@link TestScenarioContext}
 */
public interface TestScenario<TPayload, TContext extends TestScenarioContext<TPayload>> {
    /**
     * Returns a human-readable description of the scenario.
     *
     * @return the scenario description
     */
    String description();

    /**
     * Provides the payload for the scenario's action.
     *
     * @return the payload provider
     */
    PayloadProvider<TContext> payloadProvider();

    /**
     * Initializes and sets properties in the scenario context.
     *
     * @return the context provider
     */
    ContextProvider<TContext> contextProvider();

    /**
     * Sets up stubs for mocking external or internal calls.
     *
     * @return the stubs provider
     */
    StubsProvider<TContext> stubs();

    /**
     * Returns the list of assertions to validate the scenario outcome.
     *
     * @return the assertions providers
     */
    List<AssertionsProvider<TContext>> assertions();

    /**
     * Provides the action to execute within the scenario.
     *
     * @return the action provider
     */
    ActionProvider<TPayload, TContext> actionProvider();

    /**
     * Returns the scenario context.
     *
     * @return the scenario context
     */
    TContext context();

    /**
     * Returns the class type of the scenario context.
     *
     * @return the scenario context class
     */
    @SuppressWarnings("unchecked")
    default Class<TContext> contextClass() {
        return (Class<TContext>) context().getClass();
    }
}