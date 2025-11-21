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

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class TestScenarioPipeTest {

    @Test
    void ofStoresValue() {
        final var pipe = TestScenarioPipe.of("hello");
        assertEquals("hello", pipe.value());
    }

    @Test
    void pipeTransformsValue() {
        final var pipe = TestScenarioPipe.of("foo");
        final var intPipe = pipe.pipe(String::length);
        assertEquals(3, intPipe.value());
    }

    @Test
    void pipeCanChainMultipleTimes() {
        final var pipe = TestScenarioPipe.of("foo");
        final var result = pipe.pipe(s -> s + "bar").pipe(String::toUpperCase);
        assertEquals("FOOBAR", result.value());
    }

    @Test
    void executeRunsConsumer() {
        final var ref = new AtomicReference<String>();
        TestScenarioPipe.of("baz").execute(ref::set);
        assertEquals("baz", ref.get());
    }
}
