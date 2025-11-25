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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for configuring a test scenario in Spring-based scenario testing.
 * <p>
 * {@code @ConfigureTestScenario} marks a class as a test scenario definition and specifies
 * the bean names for the providers (stubs, context, payload, action) to use for setup and execution.
 * Beans are referenced by name, allowing flexible and modular scenario configuration.
 * </p>
 *
 * <p>
 * Optional attributes such as {@code description}, {@code group}, and {@code name} provide
 * metadata and organization for scenarios. The {@code context} attribute specifies the scenario context class.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * &#64;ConfigureTestScenario(
 *     stubsProvider = "myStubsProviderBean",
 *     contextProvider = "myContextProviderBean",
 *     payloadProvider = "myPayloadProviderBean",
 *     actionProvider = "myActionProviderBean",
 *     description = "Test scenario for user registration",
 *     group = "user",
 *     name = "registerUser",
 *     context = UserScenarioContext.class
 * )
 * public class RegisterUserScenario { ... }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigureTestScenario {
    /**
     * The bean name of the stubs provider to use in this scenario.
     * @return the stubs provider bean name
     */
    String stubsProvider();

    /**
     * The bean name of the context provider to use in this scenario.
     * @return the context provider bean name
     */
    String contextProvider();

    /**
     * The bean name of the payload provider to use in this scenario.
     * @return the payload provider bean name
     */
    String payloadProvider();

    /**
     * The bean name of the action provider to use in this scenario.
     * @return the action provider bean name
     */
    String actionProvider();

    /**
     * A human-readable description of the scenario.
     * @return the scenario description
     */
    String description() default "";

    /**
     * The group this scenario belongs to (for organization).
     * @return the scenario group
     */
    String group() default "";

    /**
     * The name of the scenario.
     * @return the scenario name
     */
    String name() default "";

    /**
     * The scenario context class to use.
     * @return the scenario context class
     */
    Class<?> context() default Object.class;
}