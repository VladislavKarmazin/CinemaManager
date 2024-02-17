package entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Movie {
    @NonNull
    private Integer movieId;
    private String title;
    private Integer year;
    private String genre;
    private String description;
    // One-to-One
    private Director director;
    // One-to-Many
    private List<Review> reviews;

    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }
}