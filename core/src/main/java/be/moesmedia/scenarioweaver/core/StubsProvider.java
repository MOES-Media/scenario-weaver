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
 * Sets up stubs for mocking external or internal calls within a scenario.
 * <p>
 * Implementations of this interface configure stubs using the scenario context,
 * allowing you to mock dependencies such as external services, APIs, or other components
 * required for scenario execution.
 * </p>
 *
 * @param <TContext> the type of scenario context used to configure stubs
 */
@FunctionalInterface
public interface StubsProvider<TContext> {
    /**
     * Creates and configures stubs using the provided scenario context.
     *
     * @param context the scenario context containing relevant state or data
     * @return the updated scenario context with stubs configured
     */
    TContext create(TContext context);
}
