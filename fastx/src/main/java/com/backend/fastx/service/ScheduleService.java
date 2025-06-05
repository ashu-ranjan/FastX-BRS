package com.backend.fastx.service;

import com.backend.fastx.dto.BusScheduleDto;
import com.backend.fastx.enums.ScheduleDays;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.BusRoute;
import com.backend.fastx.model.Schedule;
import com.backend.fastx.repository.BusRepository;
import com.backend.fastx.repository.BusRouteRepository;
import com.backend.fastx.repository.ScheduleRepository;
import com.backend.fastx.utility.FareUtility;
import com.backend.fastx.utility.ScheduleUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleUtility scheduleUtility;
    private final BusRepository busRepository;
    private final BusRouteRepository busRouteRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository,
                           ScheduleUtility scheduleUtility,
                           BusRepository busRepository,
                           BusRouteRepository busRouteRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleUtility = scheduleUtility;
        this.busRepository = busRepository;
        this.busRouteRepository = busRouteRepository;
    }

    public Schedule createSchedule(Schedule schedule, int busId, int routeId) {
        scheduleUtility.validateSchedule(schedule);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(()-> new ResourceNotFoundException("No Bus found on given ID"));

        BusRoute busRoute = busRouteRepository.findById(routeId)
                .orElseThrow(()-> new ResourceNotFoundException("No Bus Route found on given ID"));

        schedule.setBus(bus);
        schedule.setBusRoute(busRoute);

        if (schedule.getArrivalTime().isBefore(schedule.getDepartureTime())) {
            schedule.setArrivalTime(schedule.getArrivalTime().plusHours(24));// treat as next day
        }
        Duration duration = Duration.between(schedule.getDepartureTime(), schedule.getArrivalTime());
        schedule.setDuration(duration);


        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(int id) {
        return scheduleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Invalid Id, No Schedule found!!"));
    }

    public List<BusScheduleDto> getScheduleByRouteAndDate(String origin, String destination, LocalDate date) {

        // Extracting day from the given date by customer (@RequestParam LocalDate date)
        ScheduleDays days = ScheduleDays.valueOf(date.getDayOfWeek().name());

        List<Schedule> schedules = scheduleRepository.findByOriginDestinationAndDay(origin,destination,days);

        return schedules.stream().map(schedule -> new BusScheduleDto(
                schedule.getBus().getBusName(),
                schedule.getBus().getBusType().name(),
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                schedule.getDuration(),
                FareUtility.calculateBusFare(schedule)
        )).toList();
    }
}
