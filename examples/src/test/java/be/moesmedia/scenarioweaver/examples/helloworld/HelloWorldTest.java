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

import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestCase;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestCaseProperties;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.TestExecutor;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.impl.DefaultTestExecutor;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.junit.InjectTestCase;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.junit.TestCaseSource;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.junit.TestCaseWeaverExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestCaseWeaverExtension.class)
class HelloWorldTest {

    @SuppressWarnings("unused")
    @TestCaseSource
    private final HelloWorldTestCaseProvider testCaseProvider = new HelloWorldTestCaseProvider();

    private final TestExecutor testExecutor = new DefaultTestExecutor();

    @Test
    void test_hello_world(@InjectTestCase("hello-world") TestCase<String, TestCaseProperties, String, Void> testCase) {
        testExecutor.execute(testCase);
    }
}
