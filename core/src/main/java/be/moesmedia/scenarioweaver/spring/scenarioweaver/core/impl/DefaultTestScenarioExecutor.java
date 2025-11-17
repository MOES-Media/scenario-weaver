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

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.AssertionsProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.PropertiesProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioExecutor;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioProperties;
import java.util.List;
import java.util.Objects;

public final class DefaultTestScenarioExecutor implements TestScenarioExecutor {

    @Override
    public <TPayload, TProps extends TestScenarioProperties, TResult, TContext> void execute(
            TestScenario<TPayload, TProps, TResult, TContext> testScenario) {
        if (Objects.isNull(testScenario)) {
            throw new IllegalArgumentException("TestScenario is null, we cannot execute what is not there...");
        }

        final PayloadProvider<TContext, TPayload> payloadProvider = testScenario.payloadProvider();
        final PropertiesProvider<TProps, TContext> propertiesProvider = testScenario.propertiesProvider();
        final StubsProvider<TPayload, TProps, TContext> stubsProvider = testScenario.stubs();
        final ActionProvider<TPayload, TProps, TResult> actionProvider = testScenario.actionProvider();
        final List<AssertionsProvider<TResult, TPayload, TContext>> assertions = testScenario.assertions();

        final TPayload payload = payloadProvider.create(null);
        final TProps initialProps = Objects.nonNull(propertiesProvider) ? propertiesProvider.create(null) : null;
        final TContext ctx = Objects.nonNull(stubsProvider) ? stubsProvider.create(payload, initialProps) : null;

        final TProps propsWithCtx;
        if (Objects.isNull(initialProps)) {
            propsWithCtx = Objects.nonNull(propertiesProvider) ? propertiesProvider.create(ctx) : null;
        } else {
            final TProps ctxProps = propertiesProvider.create(ctx);
            propsWithCtx = initialProps.merge(ctxProps);
        }

        final TResult result = actionProvider.execute(payload, propsWithCtx);

        assertions.forEach(assertion -> assertion.execute(result, payload, ctx));
    }
}
