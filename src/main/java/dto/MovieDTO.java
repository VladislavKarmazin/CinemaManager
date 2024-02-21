package dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class MovieDTO {
    private Integer movieId;
    private String title;
    private Integer year;
    private String genre;
    private String description;
    private DirectorDTO director;
    private List<ReviewDTO> reviews;
}