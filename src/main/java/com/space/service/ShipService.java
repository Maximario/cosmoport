package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ShipService {
    List<Ship> findAll(Pageable pageable);
    Ship add(Map<String, String> params);
    Ship updateShip(Long id, Map<String, String> params);
    String deleteById(Long id);
    List<Ship> findByParams(Map<String, String> params);
    Integer countByParams(Map<String, String> params);
    Integer count();
    Ship findById(Long id);
    boolean existsById(Long id);
    boolean isIdValid(Long id);
    boolean isParamsValid(Map<String, String> params);
    boolean isAllParamsFound(Map<String, String> params);
}
