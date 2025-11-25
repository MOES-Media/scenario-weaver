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
 * Provides an entry point for executing an action within a scenario.
 * <p>
 * Implementations of this interface define how a scenario step is triggered,
 * such as sending a request to an API, posting a message to a queue, or any other
 * operation that initiates the scenario's execution.
 * </p>
 *
 * @param <TPayload> the type of payload used to trigger the action
 * @param <TContext> the scenario context, extending {@link TestScenarioContext}, which is updated and returned after execution
 */
@FunctionalInterface
public interface ActionProvider<TPayload, TContext extends TestScenarioContext<TPayload>> {
    /**
     * Executes the scenario action using the provided payload and context.
     *
     * @param payload the payload to use for the action
     * @param context the current scenario context
     * @return the updated scenario context after the action is performed
     */
    TContext execute(TPayload payload, TContext context);
}
