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

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestCase;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestCaseProperties;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestExecutor;
import java.util.Objects;

public final class DefaultTestExecutor implements TestExecutor {

    @Override
    public <TPayload, TProps extends TestCaseProperties, TResult, TContext> void execute(
            TestCase<TPayload, TProps, TResult, TContext> testCase) {
        if (Objects.isNull(testCase)) {
            throw new IllegalArgumentException("TestCase is null, we cannot execute what is not there...");
        }
        final TPayload payload = testCase.payloadProvider().create(null);
        final TProps initialProps = Objects.isNull(testCase.propertiesProvider().create(null))
                ? null
                : testCase.propertiesProvider().create(null);

        final TContext ctx =
                Objects.isNull(testCase.stubs()) ? null : testCase.stubs().create(payload, initialProps);
        final TProps propsWithCtx = Objects.isNull(initialProps)
                ? testCase.propertiesProvider().create(ctx)
                : initialProps.merge(
                        Objects.isNull(testCase.propertiesProvider())
                                ? null
                                : testCase.propertiesProvider().create(ctx));

        final TResult result = testCase.actionProvider().execute(payload, propsWithCtx);

        testCase.assertions().forEach(assertion -> assertion.execute(result, payload, ctx));
    }
}
