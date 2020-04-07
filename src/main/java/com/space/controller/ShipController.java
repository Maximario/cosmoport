package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/ships")
public class ShipController {

    @Autowired
    private ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping()
    public @ResponseBody ResponseEntity<List<Ship>> getShipsList(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return new ResponseEntity<List<Ship>>(shipService.findAll(PageRequest.of(0, 3)), HttpStatus.OK);
        } else {
            return new ResponseEntity<List<Ship>>(shipService.findByParams(params), HttpStatus.OK);
        }
    }

    @GetMapping("/count")
    public @ResponseBody Integer getShipsCount(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return shipService.count();
        } else {
            return shipService.countByParams(params);
        }
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Ship> addShip(@RequestBody Map<String, String> params){
        if (!shipService.isAllParamsFound(params) || !shipService.isParamsValid(params)) return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        Ship ship = shipService.add(params);
        ResponseEntity<Ship> result = ship == null ? new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST) : new ResponseEntity<Ship>(ship, HttpStatus.OK);
        return result;
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Ship> getShip(
        @PathVariable Long id
    ){
        try {
            if (!shipService.isIdValid(id)) {
                return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
            }
            if (!shipService.existsById(id)) {
                return new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Ship>(shipService.findById(id), HttpStatus.OK);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Ship> updateShip(@PathVariable Long id, @RequestBody Map<String, String> params){
        ResponseEntity<Ship> badResponse = new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Ship> nfResponse = new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
        if (!shipService.isIdValid(id) || !shipService.isParamsValid(params)) {
            return badResponse;
        }
        Ship result = shipService.updateShip(id, params);
        if (result == null) {
            return nfResponse;
        } else {
            return new ResponseEntity<Ship>(result, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<Ship> deleteShip(@PathVariable Long id){
        ResponseEntity<Ship> okResponse = new ResponseEntity<Ship>(HttpStatus.OK);
        ResponseEntity<Ship> badResponse = new ResponseEntity<Ship>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Ship> nfResponse = new ResponseEntity<Ship>(HttpStatus.NOT_FOUND);
        try {
            if (!shipService.isIdValid(id)) return badResponse;
            String result = shipService.deleteById(id);
            if (result == null) return badResponse;
            if ("404".equals(result)) return nfResponse;
            if ("200".equals(result)) return okResponse;
        } catch (NullPointerException | IllegalArgumentException e) {
            return badResponse;
        }
        return badResponse;
    }

}
