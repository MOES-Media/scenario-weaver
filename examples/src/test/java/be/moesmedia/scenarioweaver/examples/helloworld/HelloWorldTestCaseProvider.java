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
import be.moesmedia.scenarioweaver.spring.scenarioweaver.junit.TestCaseProvider;
import java.util.Optional;

public class HelloWorldTestCaseProvider implements TestCaseProvider {
    @Override
    public Optional<TestCase<String, TestCaseProperties, String, Void>> getTestCase(String name) {
        if ("hello-world".equals(name)) {
            return Optional.of(new HelloWorldTestCase());
        }
        return Optional.empty();
    }
}
