package com.example.demo.mock;

import com.example.demo.common.service.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUuidHolderTest implements UuidHolder {

    private final String uuid;
    @Override
    public String random() {
        return uuid;
    }
}
