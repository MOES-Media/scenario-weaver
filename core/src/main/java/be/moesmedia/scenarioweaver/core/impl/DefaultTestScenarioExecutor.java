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

public final class DefaultTestScenarioExecutor implements TestScenarioExecutor {

    @Override
    public <TPayload, TContext extends TestScenarioContext<TPayload>> void execute(
            TestScenario<TPayload, TContext> testScenario) {
        if (Objects.isNull(testScenario)) {
            throw new IllegalArgumentException("TestScenario is null, we cannot execute what is not there...");
        }

        TestScenarioPipe.of(testScenario.context())
                .pipe(ctx -> testScenario.payloadProvider().create(ctx))
                .pipe(ctx -> testScenario.propertiesProvider().create(ctx))
                .pipe(ctx -> testScenario.stubs().create(ctx))
                .pipe(ctx -> testScenario.actionProvider().execute(ctx.payload(), ctx))
                .execute(ctx -> testScenario.assertions().forEach(assertion -> assertion.execute(ctx)));
    }
}
