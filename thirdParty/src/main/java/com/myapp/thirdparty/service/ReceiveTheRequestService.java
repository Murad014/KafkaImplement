package com.myapp.thirdparty.service;

import com.myapp.thirdparty.data.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class ReceiveTheRequestService {
    private final UserData userData;
    private int counter = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public ReceiveTheRequestService(UserData userData) {
        this.userData = userData;
    }

    public String getUserData() throws InterruptedException {
        log.info("getUserData started...");

        if (lock.tryLock()) {
            try {
                log.info("Get Counter: {}", ++counter);
                Thread.sleep(3_000);
                log.info("Get Counter finished");
                return userData.toString();
            } finally {
                lock.unlock();
            }
        } else {
            log.warn("Another request is already processing");
            return "Request in progress, please try again later.";
        }
    }

    public void setUserData(String username) throws InterruptedException {
        log.info("setUserData started...");
        if(lock.tryLock()) {
            try {
                log.info("Set Counter: {}", ++counter);
                Thread.sleep(3_000);
                log.info("Set Counter finished");
                userData.setUsername(username);
            } finally {
                lock.unlock();
            }
        } else {
            log.warn("Another request is already processing");
        }
    }
}
