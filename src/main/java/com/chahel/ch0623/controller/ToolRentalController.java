package com.chahel.ch0623.controller;

import com.chahel.ch0623.entity.ToolRental;
import com.chahel.ch0623.service.ToolRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/toolRental")
public class ToolRentalController {

    @Autowired
    ToolRentalService toolRentalServ;

    @PostMapping
    public ResponseEntity<ToolRental> processToolRental(@RequestBody ToolRental rental) {
        return ResponseEntity.ok(toolRentalServ.addOne(rental));
    }

    @GetMapping
    public ResponseEntity<Optional<ToolRental>> retrieveRentalAgreement(@RequestParam long id) {
        return ResponseEntity.ok(toolRentalServ.findById(id));
    }
}
