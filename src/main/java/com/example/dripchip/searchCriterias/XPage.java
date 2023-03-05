package com.example.dripchip.searchCriterias;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class XPage {
    private Integer from = 0;
    private Integer size = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";
}
