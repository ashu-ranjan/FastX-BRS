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

    /**
     * Adds a new bus for a given operator.
     *
     * @param bus      The bus object to be added.
     * @param username The username of the bus operator.
     * @return The added bus object.
     * @throws ResourceNotFoundException if no operator is found for the given username.
     */

    public Bus addBus(Bus bus, String username) {

        // Fetch operator by username (having one-to-one with User)
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));

        // Set the operator to this bus
        bus.setBusOperator(operator);

        // Save and return
        return busRepository.save(bus);
    }

    /**
     * Fetches all buses associated with a given operator.
     *
     * @param username The username of the bus operator.
     * @return A list of buses associated with the operator.
     * @throws ResourceNotFoundException if no operator is found for the given username.
     */

    public List<Bus> getBusesByOperator(String username) {
        // Fetch operator by username
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));
        // Fetch buses by operator
        return busRepository.findByBusOperator(operator);
    }

    /**
     * Adds or updates amenities for a bus for a given operator.
     *
     * @param amenitiesDTO The DTO containing the amenities to be added.
     * @param username     The username of the bus operator.
     * @return The updated bus with the new amenities.
     * @throws ResourceNotFoundException if no bus or operator is found.
     */

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

        
        String amenitiesCsv = amenitiesStr.toString().replaceAll(",$", "");

        bus.setAmenities(amenitiesCsv);

        return busRepository.save(bus);
    }

    /**
     * Fetches the amenities associated with a bus for a given operator.
     *
     * @param username The username of the bus operator.
     * @return A DTO containing the amenities of the bus.
     * @throws ResourceNotFoundException if no bus or operator is found.
     */

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

    /**
     * Fetches a bus by its ID.
     *
     * @param busId The ID of the bus to be fetched.
     * @return The bus with the specified ID.
     * @throws ResourceNotFoundException if no bus is found with the given ID.
     */

    public Bus getBusById(int busId) {
        return busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with ID: " + busId));
    }

    /**
     * Updates the details of an existing bus.
     *
     * @param busId The ID of the bus to be updated.
     * @param bus   The bus object containing updated details.
     * @return The updated bus object.
     * @throws ResourceNotFoundException if no bus is found with the given ID.
     */

    public Bus updateBusDetails(int busId, Bus bus) {

        Bus existingBus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with ID: " + busId));

        existingBus.setBusName(bus.getBusName());
        existingBus.setBusNumber(bus.getBusNumber());
        existingBus.setCapacity(bus.getCapacity());
        existingBus.setBusClass(bus.getBusClass());
        existingBus.setBusType(bus.getBusType());
        existingBus.setActive(bus.isActive());

        return busRepository.save(existingBus);
    }

}