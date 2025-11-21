/*
 * scenario-weaver-junit - Scenario based testing
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
package be.moesmedia.scenarioweaver.junit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import be.moesmedia.scenarioweaver.core.TestScenario;
import java.lang.reflect.Parameter;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

class TestScenarioWeaverExtensionTest {

    private final TestScenarioWeaverExtension extension = new TestScenarioWeaverExtension();

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void supportsParameterReturnsTrueWhenAnnotatedAndAssignable() {
        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.isAnnotated(InjectTestScenario.class)).thenReturn(true);

        Parameter parameter = mock(Parameter.class);
        when(paramCtx.getParameter()).thenReturn(parameter);
        when(parameter.getType()).thenReturn((Class) TestScenario.class);

        assertTrue(extension.supportsParameter(paramCtx, extCtx));
    }

    @Test
    void supportsParameterReturnsFalseIfNotAnnotated() {
        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.isAnnotated(InjectTestScenario.class)).thenReturn(false);
        assertFalse(extension.supportsParameter(paramCtx, extCtx));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    void supportsParameterReturnsFalseIfNotAssignable() {
        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.isAnnotated(InjectTestScenario.class)).thenReturn(true);
        final var parameter = mock(java.lang.reflect.Parameter.class);
        when(paramCtx.getParameter()).thenReturn(parameter);
        when(parameter.getType()).thenReturn((Class) String.class);

        assertFalse(extension.supportsParameter(paramCtx, extCtx));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveParameterReturnsScenarioWhenFound() throws Exception {
        class Provider implements TestScenarioProvider {
            @Override
            public Optional<TestScenario<?, ?>> getTestScenario(String name) {
                return "foo".equals(name) ? Optional.of(mock(TestScenario.class)) : Optional.empty();
            }
        }
        class TestInstance {
            @TestScenarioSource
            @SuppressWarnings("unused")
            public final Provider provider = new Provider();
        }
        final var testInstance = new TestInstance();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        final var injectAnn = mock(InjectTestScenario.class);
        when(paramCtx.findAnnotation(InjectTestScenario.class)).thenReturn(Optional.of(injectAnn));
        when(injectAnn.value()).thenReturn("foo");
        when(extCtx.getRequiredTestInstance()).thenReturn(testInstance);

        Object result = extension.resolveParameter(paramCtx, extCtx);
        assertNotNull(result);
        assertInstanceOf(TestScenario.class, result);
    }

    @Test
    void resolveParameterThrowsIfNoScenarioFound() {
        class Provider implements TestScenarioProvider {
            @Override
            public Optional<TestScenario<?, ?>> getTestScenario(String name) {
                return Optional.empty();
            }
        }
        class TestInstance {
            @TestScenarioSource
            @SuppressWarnings("unused")
            public final Provider provider = new Provider();
        }
        final var testInstance = new TestInstance();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        final var injectAnn = mock(InjectTestScenario.class);
        when(paramCtx.findAnnotation(InjectTestScenario.class)).thenReturn(Optional.of(injectAnn));
        when(injectAnn.value()).thenReturn("bar");
        when(extCtx.getRequiredTestInstance()).thenReturn(testInstance);

        final var ex =
                assertThrows(ParameterResolutionException.class, () -> extension.resolveParameter(paramCtx, extCtx));
        assertTrue(ex.getMessage().contains("No TestScenario found for name: bar"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void resolveParameterHandlesNoAnnotation() {
        class Provider implements TestScenarioProvider {
            @Override
            public Optional<TestScenario<?, ?>> getTestScenario(String name) {
                if (name.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(mock(TestScenario.class));
            }
        }
        class TestInstance {
            @TestScenarioSource
            @SuppressWarnings("unused")
            public final Provider provider = new Provider();
        }
        final var testInstance = new TestInstance();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        when(paramCtx.findAnnotation(InjectTestScenario.class)).thenReturn(Optional.empty());
        when(extCtx.getRequiredTestInstance()).thenReturn(testInstance);

        final var ex =
                assertThrows(ParameterResolutionException.class, () -> extension.resolveParameter(paramCtx, extCtx));
        assertTrue(ex.getMessage().contains("No TestScenario found for name: "));
    }

    @Test
    void resolveParameterSkipsFieldIfNotTestScenarioProvider() {
        class NotAProvider {}
        class TestInstance {
            @TestScenarioSource
            @SuppressWarnings("unused")
            public final NotAProvider provider = new NotAProvider();
        }
        final var testInstance = new TestInstance();

        final var paramCtx = mock(ParameterContext.class);
        final var extCtx = mock(ExtensionContext.class);

        final var injectAnn = mock(InjectTestScenario.class);
        when(paramCtx.findAnnotation(InjectTestScenario.class)).thenReturn(Optional.of(injectAnn));
        when(injectAnn.value()).thenReturn("foo");
        when(extCtx.getRequiredTestInstance()).thenReturn(testInstance);

        final var ex =
                assertThrows(ParameterResolutionException.class, () -> extension.resolveParameter(paramCtx, extCtx));
        assertTrue(ex.getMessage().contains("No TestScenario found for name: foo"));
    }
}
