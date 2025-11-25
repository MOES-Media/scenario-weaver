/*
 * scenario-weaver-spring - Scenario based testing
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
package be.moesmedia.scenarioweaver.spring;

import be.moesmedia.scenarioweaver.core.TestScenario;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * JUnit Jupiter extension for injecting {@link be.moesmedia.scenarioweaver.core.TestScenario} beans into Spring-based tests.
 * <p>
 * {@code SpringTestScenarioWeaverExtension} enables scenario-based testing by resolving parameters
 * annotated with {@link InjectTestScenario}. It retrieves the scenario bean from the Spring {@link ApplicationContext}
 * using the bean name or scenario name specified in the annotation.
 * </p>
 *
 * <p>
 * Usage example:
 * <pre>
 * &#64;ExtendWith(SpringTestScenarioWeaverExtension.class)
 * class MySpringTest {
 *     &#64;Test
 *     void testScenario(@InjectTestScenario("registerUser") TestScenario&lt;Payload, Context&gt; scenario) {
 *         // scenario is injected from the Spring context by name "registerUser"
 *     }
 * }
 * </pre>
 */
public final class SpringTestScenarioWeaverExtension implements ParameterResolver {

    /**
     * Constructs a new {@code SpringTestScenarioWeaverExtension} instance.
     */
    public SpringTestScenarioWeaverExtension() {}

    /**
     * Determines if the parameter is eligible for injection by checking if it is annotated
     * with {@link InjectTestScenario} and is assignable to {@link TestScenario}.
     *
     * @param parameterContext the context for the parameter in the test method
     * @param extensionContext the current extension context
     * @return {@code true} if the parameter can be resolved, {@code false} otherwise
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.isAnnotated(InjectTestScenario.class)
                && TestScenario.class.isAssignableFrom(
                        parameterContext.getParameter().getType());
    }

    /**
     * Resolves the parameter by retrieving the {@link TestScenario} bean from the Spring {@link ApplicationContext}
     * using the name specified in the {@link InjectTestScenario} annotation.
     *
     * @param parameterContext the context for the parameter in the test method
     * @param extensionContext the current extension context
     * @return the resolved {@link TestScenario} bean
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        final InjectTestScenario injectTestScenario =
                parameterContext.findAnnotation(InjectTestScenario.class).orElse(null);
        final String testScenarioName = injectTestScenario.value();

        final ApplicationContext ctx = SpringExtension.getApplicationContext(extensionContext);

        return ctx.getBean(testScenarioName, TestScenario.class);
    }
}
