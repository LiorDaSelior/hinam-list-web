package com.hinamlist.hinam_list_web.controller;


import com.hinamlist.hinam_list_web.model.AlgorithmInput;
import com.hinamlist.hinam_list_web.service.algorithm_messenger.AlgorithmMessengerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AlgorithmMessengerController {

    AlgorithmMessengerService service;

    public AlgorithmMessengerController(AlgorithmMessengerService service) {
        this.service = service;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestAlgorithm(@RequestBody AlgorithmInput algorithmInput,
                                                   HttpSession session) {
        service.sendAlgorithmInput(algorithmInput, session.getId());
        return ResponseEntity.ok("Please await result...");
    }

    @GetMapping("/read")
    public ResponseEntity<String> requestAlgorithmResult(HttpSession session) {
        return service.getAlgorithmResult(session.getId());
    }
}
