package io.github.rxcats.datasourceroutedemo.fn;

/**
 * Database Operation callback
 *
 * @param <T>
 */
@FunctionalInterface
public interface DbOperation<T> {

    T run();

}
