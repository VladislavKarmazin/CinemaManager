package entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Класс для реализации One-to-One к фильмам
 */
@Getter
@Setter
public class Director {
    @NonNull
    private Integer  directorId;
    private String name;
    //One-to-One
    private Movie movie;
}
