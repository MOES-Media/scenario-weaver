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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Map;

public class TestCaseWeavingRegistrar implements ImportBeanDefinitionRegistrar, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
        final Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableTestCaseWeaving.class.getName());
        final String[] basePackages = (String[]) attributes.get("basePackages");

        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ConfigureTestCase.class));

        for (final String basePackage : basePackages) {
            for (final var beanDef : scanner.findCandidateComponents(basePackage)) {
                try {
                    final Class<?> clazz = Class.forName(beanDef.getBeanClassName());
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

                } catch (final ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}