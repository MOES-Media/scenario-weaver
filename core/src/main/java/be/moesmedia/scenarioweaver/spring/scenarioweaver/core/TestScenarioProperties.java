package be.moesmedia.scenarioweaver.spring.scenarioweaver.core;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface TestScenarioProperties {
    @SuppressWarnings("unchecked")
    default <TPropertiesType extends TestScenarioProperties> TPropertiesType merge(
            final TPropertiesType newProperties) {
        try {
            final Class<?> clazz = this.getClass();
            final TPropertiesType mergedProperties =
                    (TPropertiesType) clazz.getDeclaredConstructor().newInstance();

            Arrays.stream(clazz.getDeclaredFields())
                    .forEach(field -> copyFieldValue(field, this, newProperties, mergedProperties));

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
