/*
 * scenario-weaver-core - Scenario based testing
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
package be.moesmedia.scenarioweaver.spring.scenarioweaver.core;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface TestScenarioContext<TPayload> {
    TPayload payload();

    @SuppressWarnings("unchecked")
    default <TContext extends TestScenarioContext<TPayload>> TContext merge(final TContext context) {
        try {
            final Class<?> clazz = this.getClass();
            final TContext mergedProperties =
                    (TContext) clazz.getDeclaredConstructor().newInstance();

            Arrays.stream(clazz.getDeclaredFields())
                    .forEach(field -> copyFieldValue(field, this, context, mergedProperties));

            return mergedProperties;
        } catch (final Exception e) {
            throw new RuntimeException("Failed to merge properties", e);
        }
    }

    private static void copyFieldValue(Field field, Object original, Object newProps, Object merged) {
        try {
            field.setAccessible(true);
            final Object originalValue = field.get(original);
            final Object newValue = field.get(newProps);
            if (originalValue != null) {
                field.set(merged, originalValue);
            } else if (newValue != null) {
                field.set(merged, newValue);
            }
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
