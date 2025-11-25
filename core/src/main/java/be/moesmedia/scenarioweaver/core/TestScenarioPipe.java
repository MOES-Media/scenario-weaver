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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A fluent utility for chaining and executing the steps of a test scenario.
 * <p>
 * {@code TestScenarioPipe} enables a pipeline-style approach to scenario execution,
 * allowing you to chain transformations and actions on scenario data in a readable and maintainable way.
 * Each step can modify or act upon the scenario state, supporting a functional and fluent test flow.
 * </p>
 *
 * @param <T> the type of value carried through the pipeline
 */
public record TestScenarioPipe<T>(T value) {

    /**
     * Creates a new {@code TestScenarioPipe} with the given value.
     *
     * @param value the initial value for the pipeline
     * @param <T> the type of the value
     * @return a new {@code TestScenarioPipe} instance
     */
    public static <T> TestScenarioPipe<T> of(T value) {
        return new TestScenarioPipe<>(value);
    }

    /**
     * Transforms the current value using the provided mapping function and returns a new pipe.
     *
     * @param mapper the function to transform the value
     * @param <R> the type of the result after mapping
     * @return a new {@code TestScenarioPipe} with the mapped value
     */
    public <R> TestScenarioPipe<R> pipe(Function<? super T, ? extends R> mapper) {
        return new TestScenarioPipe<>(mapper.apply(value));
    }

    /**
     * Executes the provided consumer with the current value.
     *
     * @param runner the consumer to process the value
     */
    public void execute(Consumer<? super T> runner) {
        runner.accept(value);
    }
}
