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
package be.moesmedia.scenarioweaver.core;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Represents the context for a test scenario, holding state and data relevant to scenario execution.
 * <p>
 * The scenario context provides access to the payload and manages scenario-specific properties.
 * It supports merging with another context instance, allowing you to combine or override values
 * as needed for flexible scenario setup and execution.
 * </p>
 *
 * @param <TPayload> the type of payload associated with the scenario
 */
public interface TestScenarioContext<TPayload> {
    /**
     * Returns the payload associated with this scenario context.
     *
     * @return the scenario payload
     */
    TPayload payload();

    /**
     * Merges this context with another context of the same type.
     * <p>
     * For each field, non-null values from this context take precedence;
     * otherwise, non-null values from the provided context are used.
     * </p>
     *
     * @param context the context to merge with
     * @param <TContext> the type of scenario context
     * @return a new merged scenario context
     */
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