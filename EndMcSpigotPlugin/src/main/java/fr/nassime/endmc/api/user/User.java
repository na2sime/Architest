package fr.nassime.endmc.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private final UUID uuid;
    private String name;
    private Integer coins;

}
