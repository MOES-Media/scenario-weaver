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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

class TestScenarioWeavingRegistrarTest {

    @ConfigureTestScenario(
            stubsProvider = "stubs",
            propertiesProvider = "props",
            payloadProvider = "payload",
            actionProvider = "action",
            description = "desc",
            group = "group",
            name = "customBean")
    static class AnnotatedScenario {}

    @ConfigureTestScenario(
            stubsProvider = "stubs",
            propertiesProvider = "props",
            payloadProvider = "payload",
            actionProvider = "action",
            description = "desc",
            group = "group",
            name = "")
    static class AnnotatedScenarioNoName {}

    @Test
    void registerBeanDefinitions_registersBeans_withCustomName() {
        final var registrar = new TestScenarioWeavingRegistrar();

        final var metadata = mock(AnnotationMetadata.class);
        final var registry = mock(BeanDefinitionRegistry.class);

        final Map<String, Object> attributes = Map.of("basePackages", new String[] {"com.example"});
        when(metadata.getAnnotationAttributes(EnableTestScenarioWeaving.class.getName()))
                .thenReturn(attributes);

        try (final var scanner = mockStatic(ConfigurableTestScenarioScanner.class)) {
            scanner.when(() -> ConfigurableTestScenarioScanner.getAnnotatedClasses("com.example"))
                    .thenReturn(Set.of(AnnotatedScenario.class));

            registrar.registerBeanDefinitions(metadata, registry);

            verify(registry).registerBeanDefinition(eq("customBean"), any(GenericBeanDefinition.class));
        }
    }

    @Test
    void registerBeanDefinitions_registersBeans_withSimpleClassName() {
        final var registrar = new TestScenarioWeavingRegistrar();

        final var metadata = mock(AnnotationMetadata.class);
        final var registry = mock(BeanDefinitionRegistry.class);

        final Map<String, Object> attributes = Map.of("basePackages", new String[] {"com.example"});
        when(metadata.getAnnotationAttributes(EnableTestScenarioWeaving.class.getName()))
                .thenReturn(attributes);

        try (final var scanner = mockStatic(ConfigurableTestScenarioScanner.class)) {
            scanner.when(() -> ConfigurableTestScenarioScanner.getAnnotatedClasses("com.example"))
                    .thenReturn(Set.of(AnnotatedScenarioNoName.class));

            registrar.registerBeanDefinitions(metadata, registry);

            verify(registry).registerBeanDefinition(eq("AnnotatedScenarioNoName"), any(GenericBeanDefinition.class));
        }
    }

    @Test
    void registerBeanDefinitions_throwsIfNoAttributes() {
        final var registrar = new TestScenarioWeavingRegistrar();

        final var metadata = mock(AnnotationMetadata.class);
        final var registry = mock(BeanDefinitionRegistry.class);

        when(metadata.getAnnotationAttributes(EnableTestScenarioWeaving.class.getName()))
                .thenReturn(null);

        final var ex = assertThrows(
                IllegalArgumentException.class, () -> registrar.registerBeanDefinitions(metadata, registry));
        assertTrue(ex.getMessage().contains("@EnableTestScenarioWeaving required attribute basePackages not set"));
    }
}
