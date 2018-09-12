package com.odnolap.tst1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PageableRequest {
    private int offset;
    private int startFrom;
}
