package com.backend.fastx.service;

import com.backend.fastx.model.BusRoute;
import com.backend.fastx.repository.BusRouteRepository;
import com.backend.fastx.utility.BusRouteUtility;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;
    private final BusRouteUtility busRouteUtility;

    public BusRouteService(BusRouteRepository busRouteRepository, BusRouteUtility busRouteUtility) {
        this.busRouteRepository = busRouteRepository;
        this.busRouteUtility = busRouteUtility;
    }

    public BusRoute addRoute(BusRoute busRoute) {
        busRouteUtility.validateBusRoute(busRoute);
        return busRouteRepository.save(busRoute);
    }

    public List<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }
}
