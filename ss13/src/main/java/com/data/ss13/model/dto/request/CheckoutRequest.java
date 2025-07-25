package com.data.ss13.model.dto.request;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String receiver;
    private String address;
    private String phoneNumber;
}