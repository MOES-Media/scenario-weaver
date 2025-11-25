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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for injecting a {@link be.moesmedia.scenarioweaver.core.TestScenario} into JUnit tests by name.
 * <p>
 * This annotation can be applied to test method parameters or fields to automatically inject
 * a scenario instance, identified by its name. It enables convenient and declarative scenario
 * setup for scenario-based integration testing in JUnit.
 * </p>
 *
 * <p>
 * Usage example:
 * <pre>
 * &#64;Test
 * void testScenario(@InjectTestScenario("myScenario") TestScenario&lt;Payload, Context&gt; scenario) {
 *     // scenario is injected by name "myScenario"
 * }
 * </pre>
 *
 * @see be.moesmedia.scenarioweaver.core.TestScenario
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectTestScenario {
    /**
     * The name of the scenario to inject.
     *
     * @return the scenario name
     */
    String value() default "";
}
