package com.chahel.ch0623.controller;

import com.chahel.ch0623.entity.ToolRental;
import com.chahel.ch0623.service.ToolRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
public class ToolRentalController {

    @Autowired
    ToolRentalService toolRentalServ;

    public ResponseEntity<ToolRental> processToolRental(@RequestBody ToolRental rental) {
        return ResponseEntity.ok(toolRentalServ.addOne(rental));
    }
}
