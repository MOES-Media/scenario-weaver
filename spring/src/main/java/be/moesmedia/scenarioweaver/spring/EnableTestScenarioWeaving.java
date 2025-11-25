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
import org.springframework.context.annotation.Import;

/**
 * Enables annotation-based test scenario creation by importing {@link TestScenarioWeavingRegistrar}.
 * <p>
 * Use this annotation on a Spring configuration class to activate automatic scanning and registration
 * of test scenarios defined with {@link ConfigureTestScenario}. Specify the base packages to scan for
 * annotated scenario classes.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * &#64;EnableTestScenarioWeaving(basePackages = {"be.moesmedia.scenarioweaver.scenarios"})
 * public class ScenarioConfig { }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TestScenarioWeavingRegistrar.class)
public @interface EnableTestScenarioWeaving {
    /**
     * The base packages to scan for {@link ConfigureTestScenario}-annotated classes.
     *
     * @return array of package names to scan
     */
    String[] basePackages();
}