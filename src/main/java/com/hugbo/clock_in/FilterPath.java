package com.hugbo.clock_in;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Decorator for filter fields to route correctly through entity relations,
// i.e. shift->contract->user->id to get the shifts relating to userId
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterPath {
    String value();
}
