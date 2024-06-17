package com.wow.delivery.entity.shop;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class DayOfWeekListConverter implements AttributeConverter<List<DayOfWeek>, String> {

    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> attribute) {
        return attribute.stream()
            .map(DayOfWeek::name)
            .collect(Collectors.joining(SEPARATOR));
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(SEPARATOR))
            .map(DayOfWeek::valueOf)
            .collect(Collectors.toList());
    }
}
