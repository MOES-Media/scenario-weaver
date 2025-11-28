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

public interface TestCase<TPayload, TProps extends TestCaseProperties, TResult, TContext> {
    String description();

    PayloadProvider<TContext, TPayload> payloadProvider();

    PropertiesProvider<TProps, TContext> propertiesProvider();

    StubsProvider<TPayload, TProps, TContext> stubs();

    List<AssertionsProvider<TResult, TPayload, TContext>> assertions();

    ActionProvider<TPayload, TProps, TResult> actionProvider();
}
