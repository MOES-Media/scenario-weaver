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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import be.moesmedia.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.core.PropertiesProvider;
import be.moesmedia.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioContext;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("rawtypes")
class TestScenarioFactoryBeanTest {

    private ApplicationContext ctx;
    private StubsProvider stubsProvider;
    private PropertiesProvider propertiesProvider;
    private PayloadProvider payloadProvider;
    private ActionProvider actionProvider;

    @BeforeEach
    void setUp() {
        ctx = mock(ApplicationContext.class);
        stubsProvider = mock(StubsProvider.class);
        propertiesProvider = mock(PropertiesProvider.class);
        payloadProvider = mock(PayloadProvider.class);
        actionProvider = mock(ActionProvider.class);

        when(ctx.getBean("stubsProvider")).thenReturn(stubsProvider);
        when(ctx.getBean("propertiesProvider")).thenReturn(propertiesProvider);
        when(ctx.getBean("payloadProvider")).thenReturn(payloadProvider);
        when(ctx.getBean("actionProvider")).thenReturn(actionProvider);
    }

    static class ConfigWithAssertions {
        @Assertions
        public List<String> assertionsMethod() {
            return List.of("assertion1", "assertion2");
        }
    }

    static class ConfigWithoutAssertions {
        public void someMethod() {}
    }

    static class TestContext implements TestScenarioContext<Object> {
        @Override
        public Object payload() {
            return null;
        }
    }

    @Test
    void getObject_returnsTestScenario_withAssertions() {
        final var factoryBean = new TestScenarioFactoryBean(
                ConfigWithAssertions.class,
                "stubsProvider",
                "propertiesProvider",
                "payloadProvider",
                "actionProvider",
                "desc",
                "group",
                TestContext.class);
        factoryBean.setApplicationContext(ctx);

        final var scenario = factoryBean.getObject();

        assertEquals("desc", scenario.description());
        assertSame(payloadProvider, scenario.payloadProvider());
        assertSame(propertiesProvider, scenario.propertiesProvider());
        assertSame(stubsProvider, scenario.stubs());
        assertSame(actionProvider, scenario.actionProvider());
        assertEquals(List.of("assertion1", "assertion2"), scenario.assertions());
    }

    @Test
    void getObject_returnsTestScenario_withoutAssertions() {
        final var factoryBean = new TestScenarioFactoryBean(
                ConfigWithoutAssertions.class,
                "stubsProvider",
                "propertiesProvider",
                "payloadProvider",
                "actionProvider",
                "desc2",
                "group2",
                TestContext.class);
        factoryBean.setApplicationContext(ctx);

        final var scenario = factoryBean.getObject();

        assertEquals("desc2", scenario.description());
        assertSame(payloadProvider, scenario.payloadProvider());
        assertSame(propertiesProvider, scenario.propertiesProvider());
        assertSame(stubsProvider, scenario.stubs());
        assertSame(actionProvider, scenario.actionProvider());
        assertEquals(Collections.emptyList(), scenario.assertions());
    }

    @Test
    void getObject_throwsRuntimeException_onFailure() {
        final var factoryBean = new TestScenarioFactoryBean(
                ConfigWithAssertions.class,
                "stubsProvider",
                "propertiesProvider",
                "payloadProvider",
                "actionProvider",
                "desc",
                "group",
                TestContext.class);
        final var ex = assertThrows(RuntimeException.class, factoryBean::getObject);
        assertTrue(ex.getMessage().contains("Failed to create TestScenario bean"));
    }

    @Test
    void getObjectType_returnsTestScenarioClass() {
        final var factoryBean = new TestScenarioFactoryBean(
                ConfigWithAssertions.class,
                "stubsProvider",
                "propertiesProvider",
                "payloadProvider",
                "actionProvider",
                "desc",
                "group",
                TestContext.class);
        assertEquals(TestScenario.class, factoryBean.getObjectType());
    }
}
