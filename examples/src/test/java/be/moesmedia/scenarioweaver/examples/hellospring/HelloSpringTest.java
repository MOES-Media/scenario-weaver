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

import be.moesmedia.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.core.PropertiesProvider;
import be.moesmedia.scenarioweaver.core.StubsProvider;
import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioContext;
import be.moesmedia.scenarioweaver.core.TestScenarioExecutor;
import be.moesmedia.scenarioweaver.core.impl.DefaultTestScenarioExecutor;
import be.moesmedia.scenarioweaver.spring.EnableTestScenarioWeaving;
import be.moesmedia.scenarioweaver.spring.InjectTestScenario;
import be.moesmedia.scenarioweaver.spring.SpringTestScenarioWeaverExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@SpringBootTest(
        classes = {HelloSpringApplication.class, HelloSpringTest.MyTestConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringTestScenarioWeaverExtension.class)
class HelloSpringTest {

    private final TestScenarioExecutor executor = new DefaultTestScenarioExecutor();

    @Test
    void test_hello_spring(
            @InjectTestScenario("helloSpring") TestScenario<String, ? extends TestScenarioContext> testScenario) {
        executor.execute(testScenario);
    }

    @TestConfiguration
    @EnableTestScenarioWeaving(basePackages = "be.moesmedia.scenarioweaver.examples.hellospring")
    static class MyTestConfig {
        @Bean
        public StubsProvider<HelloSpringContext> myStubsProvider() {
            return (ctx) -> ctx;
        }

        @Bean
        public PropertiesProvider<HelloSpringContext> myPropertiesProvider() {
            return ctx -> ctx;
        }

        @Bean
        public PayloadProvider<HelloSpringContext> myPayloadProvider() {
            return ctx -> ctx;
        }

        @Bean
        public ActionProvider<Void, HelloSpringContext> myActionProvider(TestRestTemplate testRestTemplate) {
            return (ignored, ctx) -> ctx.response(testRestTemplate.getForEntity("/hello-spring", String.class));
        }
    }
}
