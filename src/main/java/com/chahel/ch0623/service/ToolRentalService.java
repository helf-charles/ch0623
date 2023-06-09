package com.chahel.ch0623.service;

import com.chahel.ch0623.entity.ToolRental;
import com.chahel.ch0623.repository.ToolRentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolRentalService {

    @Autowired
    ToolRentalRepository toolRentalRepo;

    public ToolRental addOne(ToolRental rental) {
        return toolRentalRepo.save(rental);
    }
}
