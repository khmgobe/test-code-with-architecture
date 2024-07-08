package com.example.demo.mock;

import com.example.demo.common.service.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolderTest implements ClockHolder {

    private final long millis;

    @Override
    public long mills() {

        return millis;
    }
}
