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
import org.springframework.context.annotation.Bean;

/**
 * Meta annotation for defining a {@link be.moesmedia.scenarioweaver.core.TestScenario} bean
 * in bean-based configuration.
 * <p>
 * Methods annotated with {@code @TestScenarioBean} are recognized as factory methods for
 * test scenario beans, enabling integration with Spring's bean lifecycle and configuration.
 * </p>
 *
 * <p>
 * Usage example:
 * <pre>
 * &#64;TestScenarioBean
 * public TestScenario&lt;Payload, Context&gt; myScenario() {
 *     // return a configured TestScenario instance
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Bean
public @interface TestScenarioBean {}
