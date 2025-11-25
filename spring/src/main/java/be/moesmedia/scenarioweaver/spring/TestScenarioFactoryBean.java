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

/**
 * {@link FactoryBean} implementation for creating {@link TestScenario} instances using annotation-based configuration in Spring.
 * <p>
 * {@code TestScenarioFactoryBean} constructs a {@link TestScenario} by looking up provider beans
 * (stubs, context, payload, action) by name from the Spring {@link ApplicationContext}, as specified
 * in the {@link ConfigureTestScenario} annotation. It also invokes any method annotated with {@link Assertions}
 * on the configuration class to retrieve assertion providers.
 * </p>
 *
 * <p>
 * This factory enables flexible and modular scenario setup, supporting annotation-driven configuration
 * and integration with the Spring bean lifecycle.
 * </p>
 *
 * <p>
 * Usage: This bean is registered automatically when using {@link EnableTestScenarioWeaving} and
 * {@link ConfigureTestScenario} for annotation-based scenario creation.
 * </p>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class TestScenarioFactoryBean implements FactoryBean<TestScenario<?, ?>>, ApplicationContextAware {
    private final Class<?> configClass;
    private final Class<? extends TestScenarioContext<?>> contextClass;
    private final String stubsProviderName;
    private final String contextProviderName;
    private final String payloadProviderName;
    private final String actionProviderName;
    private final String description;
    private final String group;

    private ApplicationContext ctx;

    /**
     * Constructs a new {@code TestScenarioFactoryBean}.
     *
     * @param configClass the configuration class annotated with {@link ConfigureTestScenario}
     * @param stubsProviderName the bean name of the stubs provider
     * @param contextProviderName the bean name of the context provider
     * @param payloadProviderName the bean name of the payload provider
     * @param actionProviderName the bean name of the action provider
     * @param description the scenario description
     * @param group the scenario group
     * @param contextClass the scenario context class
     */
    public TestScenarioFactoryBean(
            Class<?> configClass,
            String stubsProviderName,
            String contextProviderName,
            String payloadProviderName,
            String actionProviderName,
            String description,
            String group,
            Class<? extends TestScenarioContext<?>> contextClass) {
        this.configClass = configClass;
        this.stubsProviderName = stubsProviderName;
        this.contextProviderName = contextProviderName;
        this.payloadProviderName = payloadProviderName;
        this.actionProviderName = actionProviderName;
        this.description = description;
        this.group = group;
        this.contextClass = contextClass;
    }

    /**
     * Sets the Spring {@link ApplicationContext} for bean lookups.
     *
     * @param applicationContext the application context
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    /**
     * Creates the {@link TestScenario} instance by looking up providers and assertions from the context and config class.
     *
     * @return the constructed {@link TestScenario} instance
     * @throws RuntimeException if scenario creation fails
     */
    @Override
    public TestScenario<?, ?> getObject() {
        try {
            final Object stubsProvider = ctx.getBean(stubsProviderName);
            final Object contextProvider = ctx.getBean(contextProviderName);
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
                    return (ContextProvider) contextProvider;
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

    /**
     * Returns the type of object produced by this factory.
     *
     * @return the {@link TestScenario} class
     */
    @Override
    public Class<?> getObjectType() {
        return TestScenario.class;
    }
}
