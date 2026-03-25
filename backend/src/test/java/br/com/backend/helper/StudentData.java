package br.com.backend.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class StudentData {

    private UUID id;
    private String name;
    private String email;
    private String password;
}
