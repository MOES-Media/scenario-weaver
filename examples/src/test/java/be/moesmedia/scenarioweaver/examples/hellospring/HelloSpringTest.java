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
package be.moesmedia.scenarioweaver.examples.hellospring;

import be.moesmedia.scenarioweaver.spring.EnableTestScenarioWeaving;
import be.moesmedia.scenarioweaver.spring.InjectTestScenario;
import be.moesmedia.scenarioweaver.spring.SpringTestScenarioWeaverExtension;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.*;
import be.moesmedia.scenarioweaver.spring.scenarioweaver.core.impl.DefaultTestScenarioExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

@SpringBootTest(
        classes = {HelloSpringApplication.class, HelloSpringTest.MyTestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringTestScenarioWeaverExtension.class)
class HelloSpringTest {

    private final TestScenarioExecutor executor = new DefaultTestScenarioExecutor();

    @Test
    void test_hello_spring(
            @InjectTestScenario("helloSpring")
                    TestScenario<String, TestScenarioProperties, String, Void> testScenario) {
        executor.execute(testScenario);
    }

    @TestConfiguration
    @EnableTestScenarioWeaving(basePackages = "be.moesmedia.scenarioweaver.examples.hellospring")
    static class MyTestConfig {
        @Bean
        public StubsProvider<Void, TestScenarioProperties, Void> myStubsProvider() {
            return (payload, properties) -> null;
        }

        @Bean
        public PropertiesProvider<TestScenarioProperties, Void> myPropertiesProvider() {
            return props -> null;
        }

        @Bean
        public PayloadProvider<Void, Void> myPayloadProvider() {
            return ctx -> null;
        }

        @Bean
        public ActionProvider<Void, Void, ResponseEntity<String>> myActionProvider(TestRestTemplate testRestTemplate) {
            return (payload, props) -> testRestTemplate.getForEntity("/hello-spring", String.class);
        }
    }
}
