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

import be.moesmedia.scenarioweaver.junit.InjectTestScenario;
import be.moesmedia.scenarioweaver.junit.TestScenarioSource;
import be.moesmedia.scenarioweaver.junit.TestScenarioWeaverExtension;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioExecutor;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestScenarioProperties;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.impl.DefaultTestScenarioExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestScenarioWeaverExtension.class)
class HelloWorldTest {

    @SuppressWarnings("unused")
    @TestScenarioSource
    private final HelloWorldTestScenarioProvider testScenarioProvider = new HelloWorldTestScenarioProvider();

    private final TestScenarioExecutor testScenarioExecutor = new DefaultTestScenarioExecutor();

    @Test
    void test_hello_world(
            @InjectTestScenario("hello-world")
                    TestScenario<String, TestScenarioProperties, String, Void> testScenario) {
        testScenarioExecutor.execute(testScenario);
    }
}
