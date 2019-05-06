package fr.xephi.authme.security.crypts.description;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Recommendation
{
  Usage value();
}


/* Location:              C:\Users\Leonid\Downloads\AuthMe-5_4_0_jarSave.jar!\fr\xephi\authme\security\crypts\description\Recommendation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */