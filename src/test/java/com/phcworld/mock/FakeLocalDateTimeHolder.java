package com.phcworld.mock;

import com.phcworld.common.service.LocalDateTimeHolder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class FakeLocalDateTimeHolder implements LocalDateTimeHolder {

    private final LocalDateTime now;

    @Override
    public LocalDateTime now() {
        return now;
    }
}
