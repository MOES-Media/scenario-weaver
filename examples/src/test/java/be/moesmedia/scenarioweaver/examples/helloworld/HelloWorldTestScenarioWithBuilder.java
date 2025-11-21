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

import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioBuilder;
import java.util.List;

public class HelloWorldTestScenarioWithBuilder {

    static TestScenario<String, HelloWorldContext> testScenario() {
        return TestScenarioBuilder.<String, HelloWorldContext>withAction((payload, ctx) -> ctx.actual("Hello World!"))
                .assertionsProviders(List.of(ctx -> assertEquals(ctx.expected(), ctx.actual())))
                .context(new HelloWorldContext())
                .build();
    }
}
