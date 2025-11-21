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
package be.moesmedia.scenarioweaver.examples.testcontainers.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.moesmedia.scenarioweaver.core.AssertionsProvider;
import be.moesmedia.scenarioweaver.examples.testcontainers.person.PersonRepository;
import be.moesmedia.scenarioweaver.examples.testcontainers.scenarios.PersonScenarios.PersonScenarioContext;
import java.util.Objects;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@TestConfiguration
public class AssertionProviders {

    @Bean
    AssertionsProvider<PersonScenarioContext> responseAssertions() {
        return ctx -> {
            assertEquals(HttpStatus.OK, ctx.response().getStatusCode());
            assertNotNull(ctx.response().getBody());
            assertNotNull(ctx.response().getBody().id());
        };
    }
    ;

    @Bean
    AssertionsProvider<PersonScenarioContext> databaseAssertions(PersonRepository personRepository) {
        return ctx -> {
            assert Objects.nonNull(ctx.response().getBody());
            final var dbRecord =
                    personRepository.findById(ctx.response().getBody().id());
            assertTrue(dbRecord.isPresent());
            assertEquals(dbRecord.get().firstName(), ctx.response().getBody().firstName());
            assertEquals(dbRecord.get().lastName(), ctx.response().getBody().lastName());
        };
    }
}
