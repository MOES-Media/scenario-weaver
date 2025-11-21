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
package be.moesmedia.scenarioweaver.spring.scenarioweaver.core;

import java.util.List;

public interface TestScenario<TPayload, TContext extends TestScenarioContext<TPayload>> {
    String description();

    PayloadProvider<TContext> payloadProvider();

    PropertiesProvider<TContext> propertiesProvider();

    StubsProvider<TContext> stubs();

    List<AssertionsProvider<TContext>> assertions();

    ActionProvider<TPayload, TContext> actionProvider();

    TContext context();

    @SuppressWarnings("unchecked")
    default Class<TContext> contextClass() {
        return (Class<TContext>) context().getClass();
    }
}
