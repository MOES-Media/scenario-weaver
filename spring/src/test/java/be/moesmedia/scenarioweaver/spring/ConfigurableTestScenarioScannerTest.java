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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

class ConfigurableTestScenarioScannerTest {

    @ConfigureTestScenario(
            stubsProvider = "stubs",
            contextProvider = "props",
            payloadProvider = "payload",
            actionProvider = "action",
            description = "desc",
            group = "group")
    static class AnnotatedClass {}

    @Test
    void getAnnotatedClasses_returnsAnnotatedClasses() throws Exception {
        try (final var mockedScanner =
                mockConstruction(ClassPathScanningCandidateComponentProvider.class, (scanner, context) -> {
                    final var beanDef = mock(BeanDefinition.class);
                    when(beanDef.getBeanClassName()).thenReturn(AnnotatedClass.class.getName());
                    when(scanner.findCandidateComponents("com.example")).thenReturn(Set.of(beanDef));
                })) {

            final var result = ConfigurableTestScenarioScanner.getAnnotatedClasses("com.example");
            assertEquals(1, result.size());
            assertTrue(result.contains(AnnotatedClass.class));
        }
    }

    @Test
    void getAnnotatedClasses_throwsRuntimeException_whenClassNotFound() {
        try (final var mockedScanner =
                mockConstruction(ClassPathScanningCandidateComponentProvider.class, (scanner, context) -> {
                    final var beanDef = mock(BeanDefinition.class);
                    when(beanDef.getBeanClassName()).thenReturn("not.found.ClassName");
                    when(scanner.findCandidateComponents("com.example")).thenReturn(Set.of(beanDef));
                })) {

            final var ex = assertThrows(
                    RuntimeException.class, () -> ConfigurableTestScenarioScanner.getAnnotatedClasses("com.example"));
            assertTrue(ex.getMessage().contains("Failed to load class: not.found.ClassName"));
        }
    }

    @Test
    void getAnnotatedClasses_returnsEmptySet_whenNoCandidates() {
        try (final var mockedScanner =
                mockConstruction(ClassPathScanningCandidateComponentProvider.class, (scanner, context) -> {
                    when(scanner.findCandidateComponents("com.empty")).thenReturn(Collections.emptySet());
                })) {
            final var result = ConfigurableTestScenarioScanner.getAnnotatedClasses("com.empty");
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void privateConstructor_coverage() throws Exception {
        final var constructor = ConfigurableTestScenarioScanner.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
