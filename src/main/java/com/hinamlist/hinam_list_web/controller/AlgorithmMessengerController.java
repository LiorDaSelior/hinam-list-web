package com.hinamlist.hinam_list_web.controller;


import com.hinamlist.hinam_list_web.model.algorithm_messenger.ControllerUserInput;
import com.hinamlist.hinam_list_web.service.algorithm_messenger.AlgorithmMessenger;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AlgorithmMessengerController {

    AlgorithmMessenger service;

    public AlgorithmMessengerController(AlgorithmMessenger service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestAlgorithm(@RequestBody ControllerUserInput controllerUserInput,
                                                   HttpSession session) {
        service.sendAlgorithmInput(controllerUserInput, session.getId());
        return ResponseEntity.ok("Please await result...");
    }

    @GetMapping("/read")
    public ResponseEntity<String> requestAlgorithmResult(HttpSession session) {
        return service.getAlgorithmResult(session.getId());
    }
}
