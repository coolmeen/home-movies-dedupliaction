package com.homeass.deduplication.common;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.assertj.core.annotations.Beta;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Beta
public class Pipeline<T> {

    private T value;

    private static final Pipeline<?> EMPTY = new Pipeline<>();

    private Pipeline() {
        this.value = null;
    }

    private Pipeline(T initial) {
        value = initial;
    }

    private boolean isPresent() {
        return value != null;
    }

    public T getValue() {
        return value;
    }
    public static <T> Pipeline<T> of(T initial){
        return new Pipeline<>(initial);
    }



    public <R> Pipeline<R> let(Function<T,R> mapper){
        return new Pipeline<>(mapper.apply(value));
    }

    public <R> Pipeline<T> letLast(Function<T,R> mapper){
        R res = mapper.apply(value);
        return new Pipeline<>(value);
    }

    public <R> Pipeline<R> onError(Function<T,R> mapper){

        return new Pipeline<>(mapper.apply(value));
    }


    public <R> R letAndReturn(Function<T,R> mapper){
        return let(mapper).getValue();
    }


    public <R,S> Pipeline<Tuple2<R,S>> apply(Function<T,R> mapper1, Function<T,S> mapper2) {

        R apply = mapper1.apply(value);
        S apply1 = mapper2.apply(value);
        Tuple2<R, S> tuple = Tuple.of(apply, apply1);
        return new Pipeline<>(tuple);

    }


    public Pipeline<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return predicate.test(value) ? this : empty();
    }

    public <T> Pipeline<T> empty() {
        @SuppressWarnings("unchecked")
        Pipeline<T> t = (Pipeline<T>) EMPTY;
        return t;
    }

    public Pipeline<Boolean> test(Predicate<T> predicate){

        return new Pipeline<>(predicate.test(value));

    }

    public <R> Pipeline<R> map(Function<T,R> mapper){
        return let(mapper);
    }

    public  <R> Pipeline<R> flatMap(Function<T,Pipeline<R>> mapper) {
        return mapper.apply(value);
    }
}
