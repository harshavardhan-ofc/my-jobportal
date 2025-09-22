package com.jobportal.job_portal.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotifyOnSuccess {
    String message() default "";
}
