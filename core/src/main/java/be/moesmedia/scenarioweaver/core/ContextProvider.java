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
 * Initializes and prepares the test scenario context for use in subsequent steps.
 * <p>
 * Implementations of this interface set up values in the context that can be used later,
 * for example in the {@link ActionProvider} or {@link PayloadProvider}. This allows
 * pre-configuration of state, such as setting IDs or other relevant data needed for the scenario.
 * </p>
 *
 * @param <TContext> the type of scenario context to initialize
 */
@FunctionalInterface
public interface ContextProvider<TContext extends TestScenarioContext<?>> {
    /**
     * Creates and initializes the scenario context with required values.
     *
     * @param context the scenario context to initialize
     * @return the initialized scenario context
     */
    TContext create(TContext context);
}
