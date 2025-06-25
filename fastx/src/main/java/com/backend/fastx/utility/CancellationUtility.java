package com.backend.fastx.utility;

import java.time.Duration;
import java.time.LocalDateTime;

public class CancellationUtility {
    /*
     * Calculate the cancellation charge percentage based on time before departure
     * PARAM 1: departureTime -> Schedule departure time of the bus
     * PARAM 2: cancellationTime -> time at which the cancellation is requested
     * RETURN: Cancellation charge as per percentage of the fare
     * */
    public static double getCancellationCharges(LocalDateTime departureTime, LocalDateTime cancellationTime){
        long hoursBeforeDeparture = Duration.between(cancellationTime, departureTime).toHours();
        if(hoursBeforeDeparture >= 48){
            return 0.10; // 10% charges
        } else if (hoursBeforeDeparture >= 24){
            return 0.25; // 25% charges
        } else if (hoursBeforeDeparture >= 12){
            return 0.50; // 50% charges
        } else if (hoursBeforeDeparture >= 1){
            return 0.80; // 80% charges
        } else {
            return 1.00; // No refund
        }
    }
    /*
     * Returns the actual amount to be deducted based on the fare
     * PARAM: fare(original fare paid)
     * PARAM: getCancellationCharges()
     * RETURN: deducted amount
     *  */
    public static double refund(double fare, double cancellationRate){
        return fare * cancellationRate;
    }
}
// to be included..... in Booking service or bookingDetails service
// double chargeRate = CancellationUtil.getCancellationChargePercentage(schedule.getDepartureTime(), LocalDateTime.now());
// double refundAmount = originalFare - CancellationUtil.calculateCancellationAmount(originalFare, chargeRate);

