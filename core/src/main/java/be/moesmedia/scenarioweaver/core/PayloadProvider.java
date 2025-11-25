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
 * Provides payloads for use in the {@link ActionProvider}, utilizing the scenario context.
 * <p>
 * Implementations of this interface generate a payload based on the current scenario context,
 * allowing for pre-set values (such as database IDs) to simulate specific conditions or conflicts
 * during scenario execution.
 * </p>
 *
 * @param <TContext> the type of scenario context used to create or enrich the payload
 */
@FunctionalInterface
public interface PayloadProvider<TContext> {
    /**
     * Creates or enriches a payload using the provided scenario context.
     *
     * @param context the scenario context containing relevant state or data
     * @return the updated scenario context with the prepared payload
     */
    TContext create(TContext context);
}