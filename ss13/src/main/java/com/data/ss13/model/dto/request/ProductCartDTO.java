package com.data.ss13.model.dto.request;


import com.data.ss13.model.entity.Product;
import com.data.ss13.model.entity.User;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCartDTO {

    private UUID id;

    private User user;

    private Product product;

    private Integer quantity;
}
