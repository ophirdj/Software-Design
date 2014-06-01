package ac.il.technion.twc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate the setup method of query. Any query should have precisely
 * one constructor with this annotation.
 * 
 * @author Ziv Ronen
 * @date 29.05.2014
 * @mail akarks@gmail.com
 * 
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuerySetup {

}