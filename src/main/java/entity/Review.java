package entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Класс для реализации Many-to-One к фильмам
 */
@Getter
@Setter
public class Review {
    @NonNull
    private Integer  reviewId;
    private String text;
    // Many-to-One
    private Movie movie;
}