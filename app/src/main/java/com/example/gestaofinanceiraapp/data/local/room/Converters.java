package com.example.gestaofinanceiraapp.data.local.room;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Converters {

    // Transforma String em BigDecimal
    @TypeConverter
    public static BigDecimal fromString(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    // Transforma BigDecimal em String
    @TypeConverter
    public static String amountToString(BigDecimal amount) {
        return amount == null ? null : amount.toString();
    }

    // Transforma String em DateTime
    @TypeConverter
    public static LocalDateTime toDate(String dateString) {
        return dateString == null ? null : LocalDateTime.parse(dateString);
    }

    // Transforma DateTime em String
    @TypeConverter
    public static String toDateString(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}
