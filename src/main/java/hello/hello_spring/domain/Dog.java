package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer age;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private String gender;

    private String location;

    private String healthStatus;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;
}
