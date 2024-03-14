package com.phcworld.common.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SystemDateTimeHolder implements LocalDateTimeHolder{
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
