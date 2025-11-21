/*
 * scenario-weaver-examples - Scenario based testing
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
package be.moesmedia.scenarioweaver.examples.helloworld;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.*;
import java.util.List;

public class HelloWorldTestScenario implements TestScenario<String, TestScenarioContext, String, Void> {
    @Override
    public String description() {
        return "Hello World Test";
    }

    @Override
    public PayloadProvider<Void, String> payloadProvider() {
        return ctx -> "Hello, World!";
    }

    @Override
    public PropertiesProvider<TestScenarioContext, Void> propertiesProvider() {
        return ctx -> null;
    }

    @Override
    public StubsProvider<String, TestScenarioContext, Void> stubs() {
        return (payload, props) -> null;
    }

    @Override
    public List<AssertionsProvider<String, String, Void>> assertions() {
        return List.of((expected, actual, ctx) -> assertEquals(expected, actual));
    }

    @Override
    public ActionProvider<String, TestScenarioContext, String> actionProvider() {
        return (payload, props) -> payload;
    }
}
