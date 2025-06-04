package com.backend.fastx.utility;

import com.backend.fastx.model.Schedule;
import com.backend.fastx.model.Seat;

public class FareUtility {
    public static double calculateSeatFare(Schedule schedule, Seat seat){
        double base = schedule.getBaseFare();

        // Seat type price using switch case
        switch (seat.getSeatType()){
            case AISLE -> base += 50;
            case MIDDLE -> base += 70;
            case WINDOW -> base += 100;
            default -> base += 0;
        }

        // Seat Deck price
        switch (seat.getSeatDeck()){
            case LOWER -> {
                base += 0;
            }
            case UPPER -> base += 80;
            default -> base += 0;
        }

        // Bus type pricing
        switch (schedule.getBus().getBusType()){
            case NON_AC_SEATER -> base += 100;
            case AC_SEATER -> base += 200;
            case NON_AC_SEMI_SLEEPER -> base += 150;
            case AC_SEMI_SLEEPER -> base += 250;
            case NON_AC_SLEEPER -> base += 270;
            case AC_SLEEPER -> base += 300;
            default -> base += 0;
        }

        // Route-based distance pricing
        if (schedule.getBusRoute() != null && schedule.getBusRoute().getDistance() > 0){
            base += schedule.getBusRoute().getDistance() * 0.4;
        }
        return base;
    }
}
