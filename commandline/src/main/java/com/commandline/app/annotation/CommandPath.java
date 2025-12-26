package com.commandline.app.annotation;

import java.lang.annotation.Retention;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface CommandPath {
    String path() default "";
}
