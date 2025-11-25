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
 * Provides a mechanism for performing assertions using the scenario context.
 * <p>
 * Implementations of this interface define how assertions are executed
 * based on the current state of the scenario context, allowing validation
 * of outcomes and conditions within a scenario.
 * </p>
 *
 * @param <TContext> the type of scenario context used for assertions
 */
@FunctionalInterface
public interface AssertionsProvider<TContext> {
    /**
     * Executes assertions using the provided scenario context.
     *
     * @param context the scenario context containing state and data for validation
     */
    void execute(TContext context);
}
