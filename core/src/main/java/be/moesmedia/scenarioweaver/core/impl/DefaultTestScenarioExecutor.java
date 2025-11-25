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

import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioContext;
import be.moesmedia.scenarioweaver.core.TestScenarioExecutor;
import be.moesmedia.scenarioweaver.core.TestScenarioPipe;
import java.util.Objects;

/**
 * The default implementation of {@link TestScenarioExecutor}, orchestrating the execution of a test scenario.
 * <p>
 * This executor coordinates the full lifecycle of a scenario by sequentially invoking each configured provider:
 * <ul>
 *   <li>Initializes the scenario context.</li>
 *   <li>Applies the context provider to set up initial properties.</li>
 *   <li>Uses the payload provider to prepare the payload within the context.</li>
 *   <li>Configures stubs for mocking external or internal dependencies.</li>
 *   <li>Executes the main action of the scenario using the prepared payload and context.</li>
 *   <li>Runs all assertions to validate the outcome of the scenario.</li>
 * </ul>
 * The execution is performed in a fluent, pipeline-style manner using {@link TestScenarioPipe},
 * ensuring each step receives the updated context from the previous step.
 */
public final class DefaultTestScenarioExecutor implements TestScenarioExecutor {

    /**
     * Constructs a new {@code DefaultTestScenarioExecutor} instance.
     */
    public DefaultTestScenarioExecutor() {}

    /**
     * Executes the given test scenario by chaining context initialization, payload preparation,
     * stubbing, action execution, and assertions.
     *
     * @param testScenario the scenario to execute
     * @param <TPayload> the type of payload used in the scenario
     * @param <TContext> the type of scenario context
     * @throws IllegalArgumentException if {@code testScenario} is {@code null}
     */
    @Override
    public <TPayload, TContext extends TestScenarioContext<TPayload>> void execute(
            TestScenario<TPayload, TContext> testScenario) throws IllegalArgumentException {
        if (Objects.isNull(testScenario)) {
            throw new IllegalArgumentException("TestScenario is null, we cannot execute what is not there...");
        }

        TestScenarioPipe.of(testScenario.context())
                .pipe(ctx -> testScenario.contextProvider().create(ctx))
                .pipe(ctx -> testScenario.payloadProvider().create(ctx))
                .pipe(ctx -> testScenario.stubs().create(ctx))
                .pipe(ctx -> testScenario.actionProvider().execute(ctx.payload(), ctx))
                .execute(ctx -> testScenario.assertions().forEach(assertion -> assertion.execute(ctx)));
    }
}
