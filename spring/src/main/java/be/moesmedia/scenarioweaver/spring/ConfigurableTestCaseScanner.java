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

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class ConfigurableTestCaseScanner {
    private ConfigurableTestCaseScanner() {}

    public static Set<Class<?>> getAnnotatedClasses(final String basePackage) {
        final ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ConfigureTestCase.class));

        final Set<Class<?>> classes = new HashSet<>();
        for (final BeanDefinition beanDef : scanner.findCandidateComponents(basePackage)) {
            try {
                classes.add(Class.forName(beanDef.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Failed to load class: " + beanDef.getBeanClassName(), e);
            }
        }
        return classes;
    }
}
