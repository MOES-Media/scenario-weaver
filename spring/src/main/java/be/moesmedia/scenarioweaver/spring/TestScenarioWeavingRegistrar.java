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

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Spring {@link ImportBeanDefinitionRegistrar} for registering test scenario beans based on annotation configuration.
 * <p>
 * {@code TestScenarioWeavingRegistrar} is activated by {@link EnableTestScenarioWeaving} and scans the specified base packages
 * for classes annotated with {@link ConfigureTestScenario}. For each such class, it registers a {@link TestScenarioFactoryBean}
 * in the Spring context, using the configuration specified in the annotation.
 * </p>
 *
 * <p>
 * The bean name is determined by the {@code name} attribute of {@link ConfigureTestScenario}, or defaults to the class name.
 * This enables annotation-driven, modular, and automatic registration of test scenarios for scenario-based testing.
 */
public final class TestScenarioWeavingRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * Constructs a new {@code TestScenarioWeavingRegistrar} instance.
     */
    public TestScenarioWeavingRegistrar() {}

    /**
     * Registers {@link TestScenarioFactoryBean} definitions for each class annotated with {@link ConfigureTestScenario}
     * in the specified base packages.
     *
     * @param importingClassMetadata metadata of the importing class
     * @param registry the bean definition registry
     * @throws IllegalArgumentException if {@code basePackages} attribute is not set
     */
    @Override
    public void registerBeanDefinitions(
            final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
        final Map<String, Object> attributes = Optional.ofNullable(
                        importingClassMetadata.getAnnotationAttributes(EnableTestScenarioWeaving.class.getName()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "@EnableTestScenarioWeaving required attribute basePackages not set"));
        final String[] basePackages = (String[]) attributes.get("basePackages");

        for (final String basePackage : basePackages) {
            for (final var clazz : ConfigurableTestScenarioScanner.getAnnotatedClasses(basePackage)) {
                final ConfigureTestScenario config = clazz.getAnnotation(ConfigureTestScenario.class);

                final GenericBeanDefinition def = new GenericBeanDefinition();
                def.setBeanClass(TestScenarioFactoryBean.class);
                def.getConstructorArgumentValues().addIndexedArgumentValue(0, clazz);
                def.getConstructorArgumentValues().addIndexedArgumentValue(1, config.stubsProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(2, config.contextProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(3, config.payloadProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(4, config.actionProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(5, config.description());
                def.getConstructorArgumentValues().addIndexedArgumentValue(6, config.group());
                def.getConstructorArgumentValues().addIndexedArgumentValue(7, config.context());

                final String beanName = !config.name().isEmpty() ? config.name() : clazz.getSimpleName();
                registry.registerBeanDefinition(beanName, def);
            }
        }
    }
}
