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
package be.moesmedia.scenarioweaver.spring.scenarioweaver.core.impl;

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioProperties;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioExecutor;
import java.util.Objects;

public final class DefaultTestScenarioExecutor implements TestScenarioExecutor {

    @Override
    public <TPayload, TProps extends TestScenarioProperties, TResult, TContext> void execute(
            TestScenario<TPayload, TProps, TResult, TContext> testScenario) {
        if (Objects.isNull(testScenario)) {
            throw new IllegalArgumentException("TestScenario is null, we cannot execute what is not there...");
        }
        final TPayload payload = testScenario.payloadProvider().create(null);
        final TProps initialProps = Objects.isNull(testScenario.propertiesProvider().create(null))
                ? null
                : testScenario.propertiesProvider().create(null);

        final TContext ctx =
                Objects.isNull(testScenario.stubs()) ? null : testScenario.stubs().create(payload, initialProps);
        final TProps propsWithCtx = Objects.isNull(initialProps)
                ? testScenario.propertiesProvider().create(ctx)
                : initialProps.merge(
                        Objects.isNull(testScenario.propertiesProvider())
                                ? null
                                : testScenario.propertiesProvider().create(ctx));

        final TResult result = testScenario.actionProvider().execute(payload, propsWithCtx);

        testScenario.assertions().forEach(assertion -> assertion.execute(result, payload, ctx));
    }
}
