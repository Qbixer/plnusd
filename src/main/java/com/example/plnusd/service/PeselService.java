package com.example.plnusd.service;

import com.example.plnusd.exception.PeselParsingException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class PeselService {

    public static Map<Integer,Integer> peselMonthYearMapping = Map.of(
            80,1800,
            0,1900,
            20,2000,
            40,2100,
            60,2200
    );

    public LocalDate getLocalDateFromPesel(String pesel) {
        int rawYear = Integer.parseInt(pesel.substring(0,2));
        int rawMonth = Integer.parseInt(pesel.substring(2,4));
        int rawDay = Integer.parseInt(pesel.substring(4,6));
        Integer month = null;
        Integer year = null;

        for(Map.Entry<Integer,Integer> entry : peselMonthYearMapping.entrySet()) {
            if(rawMonth>entry.getKey() && rawMonth< entry.getKey()+20) {
                month = rawMonth - entry.getKey();
                year = rawYear + entry.getValue();
                break;
            }

        }
        if(month == null) {
            throw new PeselParsingException("Wrong date exception");
        }
        return LocalDate.of(year, month, rawDay);
    }
}
