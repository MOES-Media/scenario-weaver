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

import be.moesmedia.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.core.ContextProvider;
import be.moesmedia.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class TestScenarioFactoryBean implements FactoryBean<TestScenario<?, ?>>, ApplicationContextAware {
    private final Class<?> configClass;
    private final Class<? extends TestScenarioContext<?>> contextClass;
    private final String stubsProviderName;
    private final String propertiesProviderName;
    private final String payloadProviderName;
    private final String actionProviderName;
    private final String description;
    private final String group;

    private ApplicationContext ctx;

    public TestScenarioFactoryBean(
            Class<?> configClass,
            String stubsProviderName,
            String propertiesProviderName,
            String payloadProviderName,
            String actionProviderName,
            String description,
            String group,
            Class<? extends TestScenarioContext<?>> contextClass) {
        this.configClass = configClass;
        this.stubsProviderName = stubsProviderName;
        this.propertiesProviderName = propertiesProviderName;
        this.payloadProviderName = payloadProviderName;
        this.actionProviderName = actionProviderName;
        this.description = description;
        this.group = group;
        this.contextClass = contextClass;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    @Override
    public TestScenario<?, ?> getObject() {
        try {
            final Object stubsProvider = ctx.getBean(stubsProviderName);
            final Object propertiesProvider = ctx.getBean(propertiesProviderName);
            final Object payloadProvider = ctx.getBean(payloadProviderName);
            final Object actionProvider = ctx.getBean(actionProviderName);

            final var testScenarioInstance =
                    configClass.getDeclaredConstructor().newInstance();
            List<?> assertions = Collections.emptyList();
            for (final var m : configClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(be.moesmedia.scenarioweaver.spring.Assertions.class)) {
                    m.setAccessible(true);
                    assertions = (List<?>) m.invoke(testScenarioInstance);
                    break;
                }
            }

            final var finalAssertions = assertions;
            return new TestScenario<>() {
                @Override
                public String description() {
                    return description;
                }

                @Override
                public PayloadProvider payloadProvider() {
                    return (PayloadProvider) payloadProvider;
                }

                @Override
                public ContextProvider contextProvider() {
                    return (ContextProvider) propertiesProvider;
                }

                @Override
                public StubsProvider stubs() {
                    return (StubsProvider) stubsProvider;
                }

                @Override
                public List assertions() {
                    return finalAssertions;
                }

                @Override
                public ActionProvider actionProvider() {
                    return (ActionProvider) actionProvider;
                }

                @Override
                public TestScenarioContext context() {
                    try {
                        return contextClass.getDeclaredConstructor().newInstance();
                    } catch (InvocationTargetException
                            | InstantiationException
                            | IllegalAccessException
                            | NoSuchMethodException e) {
                        return null;
                    }
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Failed to create TestScenario bean for " + configClass.getName(), e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return TestScenario.class;
    }
}
