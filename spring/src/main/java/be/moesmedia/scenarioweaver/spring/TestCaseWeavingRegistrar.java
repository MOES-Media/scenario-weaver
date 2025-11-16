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

public final class TestCaseWeavingRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(
            final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
        final Map<String, Object> attributes = Optional.ofNullable(
                        importingClassMetadata.getAnnotationAttributes(EnableTestCaseWeaving.class.getName()))
                .orElseThrow(() ->
                        new IllegalArgumentException("@EnableTestCaseWeaving required attribute basePackages not set"));
        final String[] basePackages = (String[]) attributes.get("basePackages");

        for (final String basePackage : basePackages) {
            for (final var clazz : ConfigurableTestCaseScanner.getAnnotatedClasses(basePackage)) {
                final ConfigureTestCase config = clazz.getAnnotation(ConfigureTestCase.class);

                final GenericBeanDefinition def = new GenericBeanDefinition();
                def.setBeanClass(TestCaseFactoryBean.class);
                def.getConstructorArgumentValues().addIndexedArgumentValue(0, clazz);
                def.getConstructorArgumentValues().addIndexedArgumentValue(1, config.stubsProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(2, config.propertiesProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(3, config.payloadProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(4, config.actionProvider());
                def.getConstructorArgumentValues().addIndexedArgumentValue(5, config.description());
                def.getConstructorArgumentValues().addIndexedArgumentValue(6, config.group());

                final String beanName = !config.name().isEmpty() ? config.name() : clazz.getSimpleName();
                registry.registerBeanDefinition(beanName, def);
            }
        }
    }
}
