package com.example.starter.client;

import com.example.starter.model.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        //RECUPERA UM ANIME PELO ID
        ResponseEntity<Anime> anime = new RestTemplate().getForEntity("http://localhost:8080/animes/findById/5", Anime.class);
        log.info(anime);

        //RECUPERA UM ANIME PELO ID SENDO PARAM
        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/findById/{id}", Anime.class, 1); //parametros

        log.info(object);

        //STRING COM ANIMES
        Anime[] objects = new RestTemplate().getForObject("http://localhost:8080/animes/list", Anime[].class);

        log.info(Arrays.toString(objects));

        //LIST DE ANIMES
        ResponseEntity<List<Anime>> objects2 = new RestTemplate().exchange("http://localhost:8080/animes/list", HttpMethod.GET, null, new ParameterizedTypeReference<>(){});

        log.info(objects2.getBody());

        //SALVA UM ANIME
//        Anime kingdom = Anime.builder().name("kingdom").build();
//        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes/createAnime", kingdom, Anime.class);
//
//        log.info("saved anime {}", kingdomSaved );

        //SALVA UM ANIME COM POST
//        Anime samurai = Anime.builder().name("samurai X 2").build();
//        ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange("http://localhost:8080/animes/createAnime", HttpMethod.POST, new HttpEntity<>(samurai, createJsonHeader()), Anime.class);
//
//        log.info("saved anime {}", samuraiSaved );

        //PUT = update
//        Anime samuraiAnimeToUpdate = samuraiSaved.getBody();
//        samuraiAnimeToUpdate.setName("Samurai X 2 3 novamente");
//        ResponseEntity<Void> samuraiAnimeUpdated = new RestTemplate().exchange("replaceAnime", HttpMethod.PUT, new HttpEntity<>(samuraiAnimeToUpdate, createJsonHeader()), Void.class);
//
//        log.info("updated anime {}", samuraiAnimeUpdated );

        //DELETE
//        ResponseEntity<Void> samuraiDeleted = new RestTemplate().exchange("http://localhost:8080/animes/deleteById/{id}", HttpMethod.DELETE, null, Void.class, samuraiAnimeToUpdate.getId());
    }

    private static HttpHeaders createJsonHeader(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
