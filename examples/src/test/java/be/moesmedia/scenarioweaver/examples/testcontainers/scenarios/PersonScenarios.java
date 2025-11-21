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
package be.moesmedia.scenarioweaver.examples.testcontainers.scenarios;

import be.moesmedia.scenarioweaver.core.ActionProvider;
import be.moesmedia.scenarioweaver.core.AssertionsProvider;
import be.moesmedia.scenarioweaver.core.PayloadProvider;
import be.moesmedia.scenarioweaver.core.TestScenario;
import be.moesmedia.scenarioweaver.core.TestScenarioBuilder;
import be.moesmedia.scenarioweaver.core.TestScenarioContext;
import be.moesmedia.scenarioweaver.examples.testcontainers.person.Person;
import be.moesmedia.scenarioweaver.examples.testcontainers.providers.AssertionProviders;
import be.moesmedia.scenarioweaver.examples.testcontainers.providers.PersonActionProviders;
import be.moesmedia.scenarioweaver.examples.testcontainers.providers.PersonPayloadProviders;
import be.moesmedia.scenarioweaver.spring.TestScenarioBean;
import be.moesmedia.scenarioweaver.spring.TestScenarios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

@TestScenarios
@Import({PersonActionProviders.class, PersonPayloadProviders.class, AssertionProviders.class})
public class PersonScenarios {

    @Autowired
    private ActionProvider<Person, PersonScenarioContext> createPersonAction;

    @Autowired
    @Qualifier("defaultPersonPayloadProvider")
    private PayloadProvider<PersonScenarioContext> defaultPersonPayloadProvider;

    @Autowired
    @Qualifier("responseAssertions")
    private AssertionsProvider<PersonScenarioContext> responseAssertions;

    @Autowired
    @Qualifier("databaseAssertions")
    private AssertionsProvider<PersonScenarioContext> databaseAssertions;

    public static class PersonScenarioContext implements TestScenarioContext<Person> {
        private Person payload;
        private ResponseEntity<Person> response;

        @Override
        public Person payload() {
            return payload;
        }

        public ResponseEntity<Person> response() {
            return response;
        }

        public PersonScenarioContext payload(Person payload) {
            this.payload = payload;
            return this;
        }

        public PersonScenarioContext response(ResponseEntity<Person> response) {
            this.response = response;
            return this;
        }
    }

    @TestScenarioBean
    TestScenario<Person, PersonScenarioContext> createPersonTestScenario() {
        return TestScenarioBuilder.withAction(createPersonAction)
                .payloadProvider(defaultPersonPayloadProvider)
                .assertionsProviders(List.of(responseAssertions, databaseAssertions))
                .context(new PersonScenarioContext())
                .build();
    }
}
