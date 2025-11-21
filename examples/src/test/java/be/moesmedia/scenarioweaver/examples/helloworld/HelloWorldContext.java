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

import be.moesmedia.scenarioweaver.core.TestScenarioContext;

public class HelloWorldContext implements TestScenarioContext<String> {
    private String actual;
    private String expected = "Hello World!";
    private String payload;

    @Override
    public String payload() {
        return payload;
    }

    public HelloWorldContext payload(String payload) {
        this.payload = payload;
        return this;
    }

    public String expected() {
        return expected;
    }

    public HelloWorldContext expected(String expected) {
        this.expected = expected;
        return this;
    }

    public String actual() {
        return actual;
    }

    public HelloWorldContext actual(String actual) {
        this.actual = actual;
        return this;
    }
}
