/*
 * scenario-weaver-junit - Scenario based testing
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
package be.moesmedia.scenarioweaver.junit;

import be.moesmedia.scenarioweaver.core.TestScenario;
import java.util.Optional;

/**
 * Provides lookup functionality for {@link TestScenario} instances by name.
 * <p>
 * Implementations of this interface are used in conjunction with the {@link InjectTestScenario}
 * annotation to resolve and inject scenarios into JUnit tests based on their names.
 * </p>
 */
public interface TestScenarioProvider {
    /**
     * Retrieves a {@link TestScenario} by its name.
     *
     * @param name the name of the scenario to look up
     * @return an {@link Optional} containing the scenario if found, or empty if not found
     */
    Optional<? extends TestScenario<?, ?>> getTestScenario(String name);
}
