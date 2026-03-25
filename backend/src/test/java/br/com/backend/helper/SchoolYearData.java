package br.com.backend.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SchoolYearData {

    private UUID id;
    private Integer year;
}
