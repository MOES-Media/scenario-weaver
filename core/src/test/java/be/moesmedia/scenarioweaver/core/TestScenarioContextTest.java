/*
 * scenario-weaver-core - Scenario based testing
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
package be.moesmedia.scenarioweaver.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TestScenarioContextTest {

    @SuppressWarnings("rawtypes")
    static class SimpleProps implements TestScenarioContext {
        public String a;
        public String b;

        public SimpleProps() {}

        public SimpleProps(String a, String b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public Object payload() {
            return null;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void mergePrefersOriginalIfNotNull() {
        final var original = new SimpleProps("foo", null);
        final var other = new SimpleProps("bar", "baz");

        final var merged = (SimpleProps) original.merge(other);

        assertEquals("foo", merged.a);
        assertEquals("baz", merged.b);
    }

    @Test
    @SuppressWarnings("unchecked")
    void mergeUsesNewIfOriginalIsNull() {
        final var original = new SimpleProps(null, null);
        final var other = new SimpleProps("bar", "baz");

        final var merged = (SimpleProps) original.merge(other);

        assertEquals("bar", merged.a);
        assertEquals("baz", merged.b);
    }

    @Test
    @SuppressWarnings("unchecked")
    void mergeKeepsNullIfBothAreNull() {
        final var original = new SimpleProps(null, null);
        final var other = new SimpleProps(null, null);

        final var merged = (SimpleProps) original.merge(other);

        assertNull(merged.a);
        assertNull(merged.b);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void mergeThrowsIfNoDefaultConstructor() {
        record NoDefaultCtor(String x) implements TestScenarioContext {

            @Override
            public Object payload() {
                return null;
            }
        }
        final var original = new NoDefaultCtor("foo");
        final var other = new NoDefaultCtor("bar");

        final var ex = assertThrows(RuntimeException.class, () -> original.merge(other));
        assertTrue(ex.getMessage().contains("Failed to merge properties"));
    }
}
