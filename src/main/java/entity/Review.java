package entity;

import lombok.*;

/**
 * Класс для реализации Many-to-One к фильмам
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
//    @NonNull
    private Integer  reviewId;
    private String text;
    // Many-to-One
    private Movie movie;
}