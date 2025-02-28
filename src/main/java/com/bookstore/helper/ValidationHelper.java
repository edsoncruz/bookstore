package com.bookstore.helper;

import com.bookstore.exception.BadRequestException;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class ValidationHelper {

    public static void requiredValue(Object attribute, String errorMessage) {
        if (attribute instanceof String && !StringUtils.hasText((String) attribute)) {
            throw new BadRequestException(errorMessage);
        } else if (attribute == null) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void requiredNullOrEmpty(Object attribute, String errorMessage) {
        if (attribute instanceof String && StringUtils.hasText((String) attribute)) {
            throw new BadRequestException(errorMessage);
        } else if (attribute != null) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void requiredTrue(Boolean mustBeTrue, String errorMessage) {
        if (!mustBeTrue) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void requiredFalse(Boolean mustBeFalse, String errorMessage) {
        if (!mustBeFalse) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void betweenValue(BigDecimal value, BigDecimal minimumValue, BigDecimal maximumValue, String errorMessage) {
        if (value == null || value.doubleValue() < minimumValue.doubleValue() || value.doubleValue() > maximumValue.doubleValue()) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void greaterThan(Object value, Long valueToCompare, String errorMessage) {
        if (value instanceof Integer && (Integer) value <= valueToCompare) {
            throw new BadRequestException(errorMessage);

        } else if (value instanceof Long && (Long) value <= valueToCompare) {
            throw new BadRequestException(errorMessage);

        } else if (value instanceof BigDecimal && ((BigDecimal) value).doubleValue() <= valueToCompare ) {
            throw new BadRequestException(errorMessage);

        } else if (value instanceof Double && (Double) value <= valueToCompare ) {
            throw new BadRequestException(errorMessage);
        }
    }

    public static void igualOrGreaterThan(Object value, Long valueToCompare, String errorMessage){
        greaterThan(value, valueToCompare - 1, errorMessage);
    }

    public static void positiveValue(Object value, String errorMessage) {
        greaterThan(value, 0L, errorMessage);
    }

    public static void positiveValueOrNull(Object value, String errorMessage) {
        if(value != null){
            greaterThan(value, 0L, errorMessage);
        }
    }
}
