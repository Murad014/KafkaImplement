package com.myapp.thirdparty.controller;


import com.myapp.thirdparty.service.ReceiveTheRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    private final ReceiveTheRequestService receiveTheRequestService;


    public UserController(ReceiveTheRequestService receiveTheRequestService) {
        this.receiveTheRequestService = receiveTheRequestService;
    }


    @GetMapping
    public ResponseEntity<String> getUserData() throws InterruptedException {
        return ResponseEntity.ok(receiveTheRequestService.getUserData());
    }


    @GetMapping("{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username) throws InterruptedException {
        receiveTheRequestService.setUserData(username);

        return ResponseEntity.ok(receiveTheRequestService.getUserData());
    }

}
