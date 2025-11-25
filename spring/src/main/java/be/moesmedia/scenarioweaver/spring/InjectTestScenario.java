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
 * Annotation for injecting a {@link be.moesmedia.scenarioweaver.core.TestScenario} into Spring-based tests.
 * <p>
 * This annotation can be applied to test method parameters or fields to inject a scenario instance
 * by its bean name or the name specified in the {@link ConfigureTestScenario} annotation.
 * It enables convenient and declarative scenario setup for scenario-based integration testing in Spring.
 * </p>
 *
 * <p>
 * Usage example:
 * <pre>
 * &#64;Test
 * void testScenario(@InjectTestScenario("registerUser") TestScenario&lt;Payload, Context&gt; scenario) {
 *     // scenario is injected by bean name or scenario name "registerUser"
 * }
 * </pre>
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectTestScenario {
    /**
     * The bean name or scenario name to inject.
     *
     * @return the scenario identifier
     */
    String value() default "";
}
