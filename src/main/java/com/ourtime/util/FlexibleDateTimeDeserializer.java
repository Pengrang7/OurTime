package com.ourtime.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 날짜만 있는 형식(yyyy-MM-dd)과 날짜+시간 형식(yyyy-MM-dd'T'HH:mm:ss) 모두를 지원하는 Deserializer
 */
public class FlexibleDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            // 먼저 날짜+시간 형식으로 파싱 시도
            return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                // 날짜만 있는 형식으로 파싱 시도 (시간은 00:00:00으로 설정)
                LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
                return date.atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw new IOException("날짜 형식이 올바르지 않습니다: " + dateString + 
                    " (지원 형식: yyyy-MM-dd 또는 yyyy-MM-dd'T'HH:mm:ss)", ex);
            }
        }
    }
}

