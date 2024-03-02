package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDTO {
    private Integer directorId;
    private String name;

    public DirectorDTO(String name) {
        this.name = name;
    }
}