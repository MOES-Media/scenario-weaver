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

/**
 * Executes a {@link TestScenario}, automating the integration test flow.
 * <p>
 * Implementations of this interface run the provided test scenario, coordinating
 * the initialization, action execution, stubbing, and assertions as defined by the scenario.
 * This enables automated and repeatable scenario-based integration testing.
 * </p>
 */
@FunctionalInterface
public interface TestScenarioExecutor {
    /**
     * Executes the given test scenario.
     *
     * @param testScenario the scenario to execute
     * @param <TPayload> the type of payload used in the scenario
     * @param <TContext> the type of scenario context
     */
    <TPayload, TContext extends TestScenarioContext<TPayload>> void execute(
            TestScenario<TPayload, TContext> testScenario);
}