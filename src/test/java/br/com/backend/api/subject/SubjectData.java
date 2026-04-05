package br.com.backend.api.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SubjectData {

    private UUID id;
    private String name;
}
