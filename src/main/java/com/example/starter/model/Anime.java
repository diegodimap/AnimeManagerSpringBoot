package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@JsonProperty("name") caso o que vem no JSON tenha um nome diferente daqui, precisa fazer o mapeamento
    //@Column(nullable = false)
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;

}
