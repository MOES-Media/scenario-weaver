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
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * JUnit Jupiter extension for injecting {@link TestScenario} instances into test methods.
 * <p>
 * {@code TestScenarioWeaverExtension} enables scenario-based testing by resolving parameters
 * annotated with {@link InjectTestScenario}. It locates a {@link TestScenarioProvider} marked with
 * {@link TestScenarioSource} in the test class, and uses it to look up and inject the requested scenario
 * by name.
 * </p>
 *
 * <p>
 * This extension allows flexible and modular scenario injection, supporting multiple providers
 * (e.g., per nested class) and automating the setup for scenario-driven integration tests.
 * </p>
 *
 * <p>
 * Usage example:
 * <pre>
 * &#64;ExtendWith(TestScenarioWeaverExtension.class)
 * class MyTest {
 *     &#64;TestScenarioSource
 *     private final MyTestScenarioProvider provider = ...;
 *
 *     &#64;Test
 *     void testScenario(@InjectTestScenario("myScenario") TestScenario&lt;Payload, Context&gt; scenario) {
 *         // scenario is injected by name "myScenario"
 *     }
 * }
 * </pre>
 */
public final class TestScenarioWeaverExtension implements ParameterResolver {

    /**
     * Constructs a new {@code TestScenarioWeaverExtension} instance.
     */
    public TestScenarioWeaverExtension() {}

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.isAnnotated(InjectTestScenario.class)
                && TestScenario.class.isAssignableFrom(
                        parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final InjectTestScenario injectTestScenarioAnnotation =
                parameterContext.findAnnotation(InjectTestScenario.class).orElse(null);
        final String testScenarioName =
                injectTestScenarioAnnotation != null ? injectTestScenarioAnnotation.value() : "";

        final Object testInstance = extensionContext.getRequiredTestInstance();

        for (final Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(TestScenarioSource.class)) {
                field.setAccessible(true);
                try {
                    final Object provider = field.get(testInstance);
                    if (provider instanceof TestScenarioProvider) {
                        Optional<? extends TestScenario<?, ?>> testScenario =
                                ((TestScenarioProvider) provider).getTestScenario(testScenarioName);
                        if (testScenario.isPresent()) {
                            return testScenario.get();
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new ParameterResolutionException(
                            "Could not access @TestScenarioSource field: " + field.getName(), e);
                }
            }
        }
        throw new ParameterResolutionException("No TestScenario found for name: " + testScenarioName);
    }
}
