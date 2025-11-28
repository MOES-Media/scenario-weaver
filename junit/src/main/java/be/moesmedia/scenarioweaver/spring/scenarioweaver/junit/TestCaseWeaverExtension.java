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
package be.moesmedia.scenarioweaver.spring.scenarioweaver.junit;

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestCase;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public final class TestCaseWeaverExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.isAnnotated(InjectTestCase.class)
                && TestCase.class.isAssignableFrom(
                        parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final InjectTestCase injectTestCaseAnnotation =
                parameterContext.findAnnotation(InjectTestCase.class).orElse(null);
        final String testCaseName = injectTestCaseAnnotation != null ? injectTestCaseAnnotation.value() : "";

        final Object testInstance = extensionContext.getRequiredTestInstance();

        for (final Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(TestCaseSource.class)) {
                field.setAccessible(true);
                try {
                    final Object provider = field.get(testInstance);
                    if (provider instanceof TestCaseProvider) {
                        Optional<? extends TestCase<?, ?, ?, ?>> testCase =
                                ((TestCaseProvider) provider).getTestCase(testCaseName);
                        if (testCase.isPresent()) {
                            return testCase.get();
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new ParameterResolutionException(
                            "Could not access @TestCaseSource field: " + field.getName(), e);
                }
            }
        }
        throw new ParameterResolutionException("No TestCase found for name: " + testCaseName);
    }
}
