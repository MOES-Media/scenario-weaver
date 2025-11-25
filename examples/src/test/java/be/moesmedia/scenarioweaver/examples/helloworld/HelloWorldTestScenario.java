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

import be.moesmedia.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.core.AssertionsProvider;
import be.moesmedia.scenarioweaver.core.ContextProvider;
import be.moesmedia.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.core.TestScenario;
import java.util.List;

public class HelloWorldTestScenario implements TestScenario<String, HelloWorldContext> {
    @Override
    public String description() {
        return "Hello World Test";
    }

    @Override
    public PayloadProvider<HelloWorldContext> payloadProvider() {
        return ctx -> ctx.payload("Hello World!");
    }

    @Override
    public ContextProvider<HelloWorldContext> contextProvider() {
        return ctx -> ctx;
    }

    @Override
    public StubsProvider<HelloWorldContext> stubs() {
        return ctx -> ctx;
    }

    @Override
    public List<AssertionsProvider<HelloWorldContext>> assertions() {
        return List.of((ctx) -> assertEquals(ctx.expected(), ctx.actual()));
    }

    @Override
    public ActionProvider<String, HelloWorldContext> actionProvider() {
        return (payload, ctx) -> ctx.actual(payload);
    }

    @Override
    public HelloWorldContext context() {
        return new HelloWorldContext();
    }
}
