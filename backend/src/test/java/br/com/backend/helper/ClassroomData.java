package br.com.backend.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ClassroomData {

    private UUID id;
    private String name;
}
