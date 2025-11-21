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

import be.moesmedia.scenarioweaver.core.TestScenario;
import java.lang.reflect.Parameter;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

class SpringTestScenarioWeaverExtensionTest {

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void supportsParameter_returnsTrue_whenAnnotatedAndAssignable() {
        final var extension = new SpringTestScenarioWeaverExtension();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.isAnnotated(InjectTestScenario.class)).thenReturn(true);

        final var parameter = mock(Parameter.class);
        when(paramCtx.getParameter()).thenReturn(parameter);
        when(parameter.getType()).thenReturn((Class) TestScenario.class);

        assertTrue(extension.supportsParameter(paramCtx, extCtx));
    }

    @Test
    void supportsParameter_returnsFalse_whenNotAnnotated() {
        final var extension = new SpringTestScenarioWeaverExtension();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.isAnnotated(InjectTestScenario.class)).thenReturn(false);

        assertFalse(extension.supportsParameter(paramCtx, extCtx));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void supportsParameter_returnsFalse_whenNotAssignable() {
        final var extension = new SpringTestScenarioWeaverExtension();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.isAnnotated(InjectTestScenario.class)).thenReturn(true);

        final var parameter = mock(Parameter.class);
        when(paramCtx.getParameter()).thenReturn(parameter);
        when(parameter.getType()).thenReturn((Class) String.class);

        assertFalse(extension.supportsParameter(paramCtx, extCtx));
    }

    @Test
    void resolveParameter_returnsTestScenarioBean() {
        final var extension = new SpringTestScenarioWeaverExtension();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        final var injectAnn = mock(InjectTestScenario.class);
        when(paramCtx.findAnnotation(InjectTestScenario.class)).thenReturn(Optional.of(injectAnn));
        when(injectAnn.value()).thenReturn("myScenario");

        final var ctx = mock(ApplicationContext.class);
        final var scenario = mock(TestScenario.class);

        try (MockedStatic<SpringExtension> springExt = mockStatic(SpringExtension.class)) {
            springExt.when(() -> SpringExtension.getApplicationContext(extCtx)).thenReturn(ctx);
            when(ctx.getBean("myScenario", TestScenario.class)).thenReturn(scenario);

            Object result = extension.resolveParameter(paramCtx, extCtx);
            assertSame(scenario, result);
        }
    }
}
