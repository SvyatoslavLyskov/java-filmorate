package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    long id;
    @NotBlank
    @NotEmpty
    String login;
    String name;
    @Email
    @NotBlank
    String email;
    @PastOrPresent
    LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank()) {
            return login;
        }
        return name;
    }
}
