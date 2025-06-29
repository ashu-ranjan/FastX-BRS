package com.backend.fastx;

import com.backend.fastx.dto.BusScheduleDto;
import com.backend.fastx.enums.BusType;
import com.backend.fastx.enums.ScheduleDays;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.BusRoute;
import com.backend.fastx.model.Schedule;
import com.backend.fastx.repository.BusRepository;
import com.backend.fastx.repository.BusRouteRepository;
import com.backend.fastx.repository.ScheduleRepository;
import com.backend.fastx.service.ScheduleService;
import com.backend.fastx.utility.ScheduleUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleUtility scheduleUtility;

    @Mock
    private BusRepository busRepository;

    @Mock
    private BusRouteRepository busRouteRepository;

    private Bus bus;
    private BusRoute route;
    private Schedule schedule;

    @BeforeEach
    public void setup() {
        bus = new Bus();
        bus.setId(1);
        bus.setBusName("FastX Travels");
        bus.setBusType(BusType.AC_SLEEPER);

        route = new BusRoute();
        route.setId(1);
        route.setOrigin("Delhi");
        route.setDestination("Jaipur");

        schedule = new Schedule();
        schedule.setId(10);
        schedule.setDepartureTime(LocalTime.of(10, 0));
        schedule.setArrivalTime(LocalTime.of(12, 30));
        schedule.setScheduleDays(ScheduleDays.MONDAY);
        schedule.setBus(bus);
        schedule.setBusRoute(route);
    }

    @Test
    public void testCreateSchedule_Success() {
        when(busRepository.findById(1)).thenReturn(Optional.of(bus));
        when(busRouteRepository.findById(1)).thenReturn(Optional.of(route));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule created = scheduleService.createSchedule(schedule, 1, 1);
        assertEquals(bus, created.getBus());
        assertEquals(route, created.getBusRoute());
        assertNotNull(created.getDuration());

        verify(scheduleUtility, times(1)).validateSchedule(schedule);
    }

    @Test
    public void testCreateSchedule_InvalidBusId() {
        when(busRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.createSchedule(schedule, 99, 1));

        assertEquals("No Bus found on given ID", ex.getMessage());
    }

    @Test
    public void testCreateSchedule_InvalidRouteId() {
        when(busRepository.findById(1)).thenReturn(Optional.of(bus));
        when(busRouteRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.createSchedule(schedule, 1, 99));

        assertEquals("No Bus Route found on given ID", ex.getMessage());
    }

    @Test
    public void testGetAllSchedules() {
        when(scheduleRepository.findAll()).thenReturn(List.of(schedule));

        List<Schedule> result = scheduleService.getAllSchedules();

        assertEquals(1, result.size());
        assertEquals(schedule.getId(), result.get(0).getId());
    }

    @Test
    public void testGetScheduleById_Valid() {
        when(scheduleRepository.findById(10)).thenReturn(Optional.of(schedule));

        Schedule found = scheduleService.getScheduleById(10);

        assertEquals(schedule.getId(), found.getId());
    }

    @Test
    public void testGetScheduleById_Invalid() {
        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.getScheduleById(999));

        assertEquals("Invalid Id, No Schedule found!!", ex.getMessage());
    }

    @Test
    public void testGetScheduleByRouteAndDate() {
        LocalDate date = LocalDate.of(2025, 6, 30); // Assume Monday

        when(scheduleRepository.findByOriginDestinationAndDay("Delhi", "Jaipur", ScheduleDays.MONDAY))
                .thenReturn(List.of(schedule));

        List<BusScheduleDto> dtos = scheduleService.getScheduleByRouteAndDate("Delhi", "Jaipur", date);

        assertEquals(1, dtos.size());
        assertEquals(schedule.getBus().getBusName(), dtos.get(0).getBusName());
    }

    @Test
    public void testGetScheduleByBusId_Valid() {
        when(busRepository.findById(1)).thenReturn(Optional.of(bus));
        when(scheduleRepository.findByBus(bus)).thenReturn(List.of(schedule));

        List<Schedule> schedules = scheduleService.getScheduleByBusId(1);

        assertEquals(1, schedules.size());
        assertEquals(bus, schedules.get(0).getBus());
    }

    @Test
    public void testGetScheduleByBusId_InvalidBus() {
        when(busRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.getScheduleByBusId(99));

        assertEquals("No Bus found for ID: 99", ex.getMessage());
    }

    @Test
    public void testGetScheduleByBusId_NoSchedules() {
        when(busRepository.findById(1)).thenReturn(Optional.of(bus));
        when(scheduleRepository.findByBus(bus)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> scheduleService.getScheduleByBusId(1));

        assertEquals("No Schedule found for Bus ID: 1", ex.getMessage());
    }
}
