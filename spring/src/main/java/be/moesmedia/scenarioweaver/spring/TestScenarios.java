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
import org.springframework.boot.test.context.TestConfiguration;

/**
 * Meta-annotation for grouping test scenarios in bean-based configuration.
 * <p>
 * {@code @TestScenarios} is used to mark a Spring {@link org.springframework.context.annotation.Configuration}
 * class as a container for multiple {@link be.moesmedia.scenarioweaver.core.TestScenario} beans.
 * This enables logical grouping and organization of related test scenarios.
 * </p>
 *
 * <p>
 * Usage example:
 * <pre>
 * &#64;TestScenarios
 * public class UserScenarioConfig {
 *     &#64;TestScenarioBean
 *     public TestScenario&lt;Payload, Context&gt; registerUserScenario() { ... }
 *
 *     &#64;TestScenarioBean
 *     public TestScenario&lt;Payload, Context&gt; deleteUserScenario() { ... }
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestConfiguration
public @interface TestScenarios {}
