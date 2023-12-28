package com.wudgaby.platform.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/20 18:05
 * @Desc :   
 */
@UtilityClass
public class ValidatorUtils {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> Optional<String> validateEntity(T obj) {
        Set<ConstraintViolation<T>> results = validator.validate(obj);
        if (CollectionUtils.isEmpty(results)) {
            return Optional.empty();
        }

        StringBuilder sb = new StringBuilder();
        for (Iterator<ConstraintViolation<T>> iterator = results.iterator(); iterator.hasNext(); ) {
            sb.append(iterator.next().getMessage());
            if (iterator.hasNext()) {
                sb.append(" ,");
            }
        }
        return Optional.of(sb.toString());
    }

    public static <T> Optional<String> validateProperty(T obj, String propertyName) {
        Set<ConstraintViolation<T>> results = validator.validateProperty(obj, propertyName, Default.class);
        if (CollectionUtils.isEmpty(results)) {
            return Optional.empty();
        }

        StringBuilder sb = new StringBuilder();
        for (Iterator<ConstraintViolation<T>> iterator = results.iterator(); iterator.hasNext(); ) {
            sb.append(iterator.next().getMessage());
            if (iterator.hasNext()) {
                sb.append(" ,");
            }
        }
        return Optional.of(sb.toString());
    }
}
