package com.backend.fastx.service;

import com.backend.fastx.dto.BusAmenitiesDTO;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.BusOperator;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.repository.BusRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final BusOperatorRepository busOperatorRepository;

    public BusService(BusRepository busRepository, BusOperatorRepository busOperatorRepository) {
        this.busRepository = busRepository;
        this.busOperatorRepository = busOperatorRepository;
    }

    public Bus addBus(Bus bus, String username) {

        // Fetch operator by username (having one-to-one with User)
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));

        // Set the operator to this bus
        bus.setBusOperator(operator);

        // Save and return
        return busRepository.save(bus);
    }

    public List<Bus> getBusesByOperator(String username) {
        // Fetch operator by username
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));
        // Fetch buses by operator
        return busRepository.findByBusOperator(operator);
    }

    public Bus addAmenitiesToBus(BusAmenitiesDTO amenitiesDTO, String username) {
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));

        List<Bus> buses = busRepository.findByBusOperator(operator);
        if (buses.isEmpty()) {
            throw new ResourceNotFoundException("No Bus found for operator: " + username);
        }

        Bus bus = buses.get(0); // Select the first bus, or modify logic as needed

        // Convert true amenities to comma-separated string
        StringBuilder amenitiesStr = new StringBuilder();

        if (amenitiesDTO.isWifi())
            amenitiesStr.append("wifi,");
        if (amenitiesDTO.isChargingPort())
            amenitiesStr.append("chargingPort,");
        if (amenitiesDTO.isBlanket())
            amenitiesStr.append("blanket,");
        if (amenitiesDTO.isPillow())
            amenitiesStr.append("pillow,");
        if (amenitiesDTO.isFoodService())
            amenitiesStr.append("foodService,");
        if (amenitiesDTO.isEntertainmentSystem())
            amenitiesStr.append("entertainmentSystem,");
        if (amenitiesDTO.isGpsTracking())
            amenitiesStr.append("gpsTracking,");
        if (amenitiesDTO.isEmergencyExit())
            amenitiesStr.append("emergencyExit,");
        if (amenitiesDTO.isFirstAidKit())
            amenitiesStr.append("firstAidKit,");
        if (amenitiesDTO.isPowerBackup())
            amenitiesStr.append("powerBackup,");
        if (amenitiesDTO.isWaterBottle())
            amenitiesStr.append("waterBottle,");
        if (amenitiesDTO.isLuggageStorage())
            amenitiesStr.append("luggageStorage,");
        if (amenitiesDTO.isReclinerSeats())
            amenitiesStr.append("reclinerSeats,");
        if (amenitiesDTO.isAirSuspension())
            amenitiesStr.append("airSuspension,");
        if (amenitiesDTO.isFireExtinguisher())
            amenitiesStr.append("fireExtinguisher,");
        if (amenitiesDTO.isCctvCameras())
            amenitiesStr.append("cctvCameras,");

        // Remove last comma if needed
        String amenitiesCsv = amenitiesStr.toString().replaceAll(",$", "");

        bus.setAmenities(amenitiesCsv);

        return busRepository.save(bus);
    }

    public BusAmenitiesDTO getAmenitiesForBus(String username) {
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));

        List<Bus> buses = busRepository.findByBusOperator(operator);
        if (buses.isEmpty()) {
            throw new ResourceNotFoundException("No Bus found for operator: " + username);
        }

        Bus bus = buses.get(0); // Select the first bus, or modify logic as needed

        // Convert amenities string to DTO
        String[] amenitiesArray = bus.getAmenities().split(",");
        BusAmenitiesDTO amenitiesDTO = new BusAmenitiesDTO();

        for (String amenity : amenitiesArray) {
            switch (amenity.trim()) {
                case "wifi":
                    amenitiesDTO.setWifi(true);
                    break;
                case "chargingPort":
                    amenitiesDTO.setChargingPort(true);
                    break;
                case "blanket":
                    amenitiesDTO.setBlanket(true);
                    break;
                case "pillow":
                    amenitiesDTO.setPillow(true);
                    break;
                case "foodService":
                    amenitiesDTO.setFoodService(true);
                    break;
                case "entertainmentSystem":
                    amenitiesDTO.setEntertainmentSystem(true);
                    break;
                case "gpsTracking":
                    amenitiesDTO.setGpsTracking(true);
                    break;
                case "emergencyExit":
                    amenitiesDTO.setEmergencyExit(true);
                    break;
                case "firstAidKit":
                    amenitiesDTO.setFirstAidKit(true);
                    break;
                case "powerBackup":
                    amenitiesDTO.setPowerBackup(true);
                    break;
                case "waterBottle":
                    amenitiesDTO.setWaterBottle(true);
                    break;
                case "luggageStorage":
                    amenitiesDTO.setLuggageStorage(true);
                    break;
                case "reclinerSeats":
                    amenitiesDTO.setReclinerSeats(true);
                    break;
                case "airSuspension":
                    amenitiesDTO.setAirSuspension(true);
                    break;
                case "fireExtinguisher":
                    amenitiesDTO.setFireExtinguisher(true);
                    break;
                case "cctvCameras":
                    amenitiesDTO.setCctvCameras(true);
                    break;
            }
        }

        return amenitiesDTO;
    }

}