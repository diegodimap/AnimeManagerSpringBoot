package com.example.starter.integration;

import com.example.starter.model.Anime;
import com.example.starter.repository.AnimeRepository;
import com.example.starter.requests.AnimePostRequestBody;
import com.example.starter.util.AnimeCreator;
import com.example.starter.util.AnimePostRequestBodyCreator;
import com.example.starter.util.AnimePutRequestBodyCreator;
import com.example.starter.util.PageableResponse;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Delayed;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@Log4j2
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //era pra apagar mas não apaga
public class AnimeControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort //recupera a porta gerada
    private int port;

    @Autowired
    private AnimeRepository animeRepository;


    @Test
    @DisplayName("IT: list")
    //@Transactional
    void test1(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes/listpage", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();

        Assertions.assertThat(animePage).isNotNull();

        //Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(17);

        Assertions.assertThat(animePage.toList().get((int) animePage.stream().count()-1).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listall returns a list of anime when successful")
    void test2(){
        String expectedName = AnimeCreator.createAnimeToBeSaved().getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/list", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        Assertions.assertThat(animes).isNotNull();

        //Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(17);

        Assertions.assertThat(animes.get((int) animes.stream().count()-1).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by id: returns an anime when successful")
    void test3(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/findById/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Find by name: returns a list of anime when successful")
    void test4(){

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/findByName/{name}", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {}, expectedName).getBody();

        List<Anime> animes2 = animeRepository.findByName("VAI LOGO TESTES");

        //Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get((int) animes2.stream().count()-1).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by name: returns an empty list of anime when not found")
    void test5(){
        List<Anime> animes = testRestTemplate.exchange("/animes/findByName/name=123321", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Anime>>() {}).getBody();

        List<Anime> animes2 = animeRepository.findByName("VAI LOGO TESTES");

        //Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("save return anime when successful")
    void test6(){
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
        ResponseEntity<Anime> anime = testRestTemplate.postForEntity("/animes/createAnime", animePostRequestBody, Anime.class );

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(anime.getBody().getName()).isNotNull().isEqualTo("VAI LOGO TESTES");

    }

    @Test
    @DisplayName("replace update return anime when successful")
    void test7(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        savedAnime.setName("new name");

        ResponseEntity<Void> body = testRestTemplate.exchange("/animes/replaceAnime", HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);

        Assertions.assertThat(body).isNotNull();

        Assertions.assertThat(body.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete anime when successful")
    void test8(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> body = testRestTemplate.exchange("/animes/deleteById{id}", HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(body).isNotNull();

        Assertions.assertThat(body.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}
