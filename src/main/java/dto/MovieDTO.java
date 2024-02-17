package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDTO {
    private Integer movieId;
    private String title;
    private Integer year;
    private String genre;
    private String description;
    private DirectorDTO director;
    private List<ReviewDTO> reviews;
}