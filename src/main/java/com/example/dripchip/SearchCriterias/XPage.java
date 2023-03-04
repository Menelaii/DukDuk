package com.example.dripchip.SearchCriterias;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class XPage {
    private Integer from;
    private Integer size;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";
}
