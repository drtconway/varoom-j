package org.petermac.pathos.varoom;

@FunctionalInterface
public interface ThrowingConsumer<T>  {
    void accept(T x) throws Exception;
}
