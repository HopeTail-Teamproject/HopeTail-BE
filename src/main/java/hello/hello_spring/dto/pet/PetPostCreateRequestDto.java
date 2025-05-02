package hello.hello_spring.dto.pet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetPostCreateRequestDto {

    private String photoUrl;
    private String name;
    private Integer age;
    private String species;
    private String address;
    private String description;

}
