package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> findAll(Pageable pageable) {
        return shipRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Ship add(Map<String, String> params) {
        try {
            String name = params.getOrDefault("name", null);
            String planet = params.getOrDefault("planet", null);
            ShipType shipType = ShipType.valueOf(params.get("shipType"));
            Date prodDate = new Date(Long.parseLong(params.get("prodDate")));
            Boolean isUsed = params.containsKey("isUsed") && "true".equals(params.get("isUsed"));
            Double speed = Double.parseDouble(params.get("speed"));
            Integer crewSize = Integer.parseInt(params.get("crewSize"));
            Ship ship = new Ship(name, planet, shipType, prodDate, isUsed, speed, crewSize);
            ship.updateRating();
            return shipRepository.save(ship);
        } catch (NullPointerException | IllegalArgumentException | ClassCastException e) {
            return null;
        }
    }

    @Override
    public Ship updateShip(Long id, Map<String, String> params) {
        if (!shipRepository.findById(id).isPresent() || params == null) return null;
        Ship result = shipRepository.findById(id).get();
        String name = params.getOrDefault("name", null);
        String planet = params.getOrDefault("planet", null);
        ShipType shipType = params.containsKey("shipType") ? ShipType.valueOf(params.get("shipType")) : null;
        Date prodDate = params.containsKey("prodDate") ? new Date(Long.parseLong(params.get("prodDate"))) : null;
        Boolean isUsed = params.containsKey("isUsed") ? "true".equals(params.get("isUsed")) : null;
        Double speed = params.containsKey("speed") ? Double.parseDouble(params.get("speed")) : null;
        Integer crewSize = params.containsKey("crewSize") ? Integer.parseInt(params.get("crewSize")) : null;
        if (name != null) result.setName(name);
        if (planet != null) result.setPlanet(planet);
        if (shipType != null) result.setShipType(shipType);
        if (prodDate != null) result.setProdDate(prodDate);
        if (isUsed != null) result.setUsed(isUsed);
        if (speed != null) result.setSpeed(speed);
        if (crewSize != null) result.setCrewSize(crewSize);
        if (speed != null || isUsed != null || prodDate != null) result.updateRating();
        shipRepository.saveAndFlush(result);
        return result;
    }

    @Override
    public String deleteById(Long id) {
        boolean isShipExists = shipRepository.existsById(id);
        if (isShipExists) {
            shipRepository.deleteById(id);
            return "200";
        } else {
            return "404";
        }
    }

    @Override
    public Ship findById(Long id) {
        return shipRepository.findById(id).get();
    }


    @Override
    public List<Ship> findByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String planet = (String) params.getOrDefault("planet", null);
        ShipType shipType = params.containsKey("shipType") ? ShipType.valueOf((String) params.get("shipType")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean isUsed = params.containsKey("isUsed") ? Boolean.parseBoolean(params.get("isUsed")) : null;
        Double minSpeed = params.containsKey("minSpeed") ? Double.parseDouble(params.get("minSpeed")) : null;
        Double maxSpeed = params.containsKey("maxSpeed") ? Double.parseDouble(params.get("maxSpeed")) : null;
        Integer minCrewSize = params.containsKey("mincrewSize") ? Integer.parseInt(params.get("minCrewSize")) : null;
        Integer maxCrewSize = params.containsKey("maxCrewSize") ? Integer.parseInt(params.get("maxCrewSize")) : null;
        Double minRating = params.containsKey("minRating") ? Double.parseDouble(params.get("minRating")) : null;
        Double maxRating = params.containsKey("maxRating") ? Double.parseDouble(params.get("maxRating")) : null;
        Pageable pageable;
        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));
        if (params.containsKey("order")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, (ShipOrder.valueOf(params.get("order"))).getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        return shipRepository.findAllByParams(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, pageable).stream().collect(Collectors.toList());
    }

    @Override
    public Integer countByParams(Map<String, String> params) {
        String name = (String) params.getOrDefault("name", null);
        String planet = (String) params.getOrDefault("planet", null);
        ShipType shipType = params.containsKey("shipType") ? ShipType.valueOf((String) params.get("shipType")) : null;
        Calendar calendar = Calendar.getInstance();
        Date after = null;
        if (params.containsKey("after")) {
            after = new Date(Long.parseLong(params.get("after")));
        }
        Date before = null;
        if (params.containsKey("before")) {
            before = new Date(Long.parseLong(params.get("before")));
        }
        Boolean isUsed = params.containsKey("isUsed") ? Boolean.parseBoolean(params.get("isUsed")) : null;
        Double minSpeed = params.containsKey("minSpeed") ? Double.parseDouble(params.get("minSpeed")) : null;
        Double maxSpeed = params.containsKey("maxSpeed") ? Double.parseDouble(params.get("maxSpeed")) : null;
        Integer minCrewSize = params.containsKey("mincrewSize") ? Integer.parseInt(params.get("minCrewSize")) : null;
        Integer maxCrewSize = params.containsKey("maxCrewSize") ? Integer.parseInt(params.get("maxCrewSize")) : null;
        Double minRating = params.containsKey("minRating") ? Double.parseDouble(params.get("minRating")) : null;
        Double maxRating = params.containsKey("maxRating") ? Double.parseDouble(params.get("maxRating")) : null;
        return shipRepository.countByParams(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }

    @Override
    public Integer count() {
        try {
            return Math.toIntExact(shipRepository.count());
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean existsById(Long id) {
        return shipRepository.existsById(id);
    }

    @Override
    public boolean isIdValid(Long id) {
        return id > 0;
    }

    @Override
    public boolean isParamsValid(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String planet = params.getOrDefault("planet", null);
        Integer prodDate = null;
        if (params.containsKey("prodDate")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(params.get("prodDate")));
            prodDate = calendar.get(Calendar.YEAR);
        }
        Double speed = params.containsKey("speed") ? Double.parseDouble(params.get("speed")) : null;
        Integer crewSize = params.containsKey("crewSize") ? Integer.parseInt(params.get("crewSize")) : null;
        boolean result =
                (name == null || planet == null || name.length() <= 50 && name.length() > 0 && planet.length() <= 50 && planet.length() > 0)
                && (prodDate == null || prodDate >= 2800 && prodDate <= 3019)
                && (speed == null || speed > 0 && speed < 1)
                && (crewSize == null || crewSize >= 1 && crewSize <= 9999);
        try {
            if (params.containsKey("shipType")) {
                ShipType shipType = ShipType.valueOf(params.get("shipType"));
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean isAllParamsFound(Map<String, String> params) {
        return params.containsKey("name")
                && params.containsKey("planet")
                && params.containsKey("shipType")
                && params.containsKey("prodDate")
                && params.containsKey("speed")
                && params.containsKey("crewSize");
    }
}
