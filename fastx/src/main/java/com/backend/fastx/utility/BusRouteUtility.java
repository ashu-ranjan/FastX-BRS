package com.backend.fastx.utility;

import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.model.BusRoute;
import org.springframework.stereotype.Component;

@Component
public class BusRouteUtility {
    public void validateBusRoute(BusRoute busRoute){
        if (busRoute.getOrigin() == null || busRoute.getOrigin().trim().isEmpty()) {
            throw new InvalidInputException("Bus Origin is required.");
        }
        if (busRoute.getDestination() == null || busRoute.getDestination().trim().isEmpty()) {
            throw new InvalidInputException("Bus Destination is required.");
        }
    }
}
