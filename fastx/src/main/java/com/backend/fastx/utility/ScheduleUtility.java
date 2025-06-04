package com.backend.fastx.utility;

import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.model.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleUtility {

    public void validateSchedule(Schedule schedule){
        if (schedule.getDay() == null) {
            throw new InvalidInputException("Journey Date is required.");
        }
        if (schedule.getDepartureTime() == null) {
            throw new InvalidInputException("Departure Time is required.");
        }
        if (schedule.getArrivalTime() == null) {
            throw new InvalidInputException("Arrival Time is required.");
        }
    }
}
