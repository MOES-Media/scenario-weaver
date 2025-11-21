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
package be.moesmedia.scenarioweaver.examples.testcontainers;

import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioExecutor;
import be.moesmedia.scenarioweaver.core.impl.DefaultTestScenarioExecutor;
import be.moesmedia.scenarioweaver.examples.testcontainers.person.Person;
import be.moesmedia.scenarioweaver.examples.testcontainers.scenarios.PersonScenarios;
import be.moesmedia.scenarioweaver.spring.EnableTestScenarioWeaving;
import be.moesmedia.scenarioweaver.spring.InjectTestScenario;
import be.moesmedia.scenarioweaver.spring.SpringTestScenarioWeaverExtension;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.lifecycle.Startables;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TestContainersApplication.class, TestContainersTest.MyTestConfig.class})
@Import(PersonScenarios.class)
@ActiveProfiles("testcontainers-example")
@ExtendWith(SpringTestScenarioWeaverExtension.class)
public class TestContainersTest {
    private final TestScenarioExecutor executor = new DefaultTestScenarioExecutor();

    private static final MongoDBContainer MONGO_CONTAINER =
            new MongoDBContainer("mongo:6.0.18").withExposedPorts(27017);

    static {
        Startables.deepStart(Stream.of(MONGO_CONTAINER)).join();
    }

    @TestConfiguration
    @EnableTestScenarioWeaving(basePackages = "be.moesmedia.scenarioweaver.examples.testcontainers")
    static class MyTestConfig {}

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO_CONTAINER::getConnectionString);
    }

    @Test
    public void testContainers(
            @InjectTestScenario("createPersonTestScenario")
                    TestScenario<Person, PersonScenarios.PersonScenarioContext> testScenario) {
        executor.execute(testScenario);
    }
}
