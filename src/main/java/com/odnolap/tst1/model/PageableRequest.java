package com.odnolap.tst1.model;

import lombok.Data;

@Data
public abstract class PageableRequest {
    private int offset;
    private int startFrom;
}
