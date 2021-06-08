package com.example.starter.controller;

import com.example.starter.model.Anime;
import com.example.starter.requests.AnimePostRequestBody;
import com.example.starter.requests.AnimePutRequestBody;
import com.example.starter.service.AnimeService;
import com.example.starter.util.AnimeCreator;
import com.example.starter.util.AnimePostRequestBodyCreator;
import com.example.starter.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
//teste unitario
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks //classe principal
    private AnimeController animeController;

    @Mock //classes dentro da principal
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValid()));
        //um comportamento para cada teste

        //teste1
        BDDMockito.when(animeServiceMock.listAllPages(ArgumentMatchers.any())).thenReturn(animePage);

        //teste2
        BDDMockito.when(animeServiceMock.listAll()).thenReturn(List.of(AnimeCreator.createAnimeValid()));

        //teste3
        BDDMockito.when(animeServiceMock.findById(ArgumentMatchers.anyLong())).thenReturn(AnimeCreator.createAnimeValid());

        //teste4
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createAnimeValid()));

        //teste6
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class))).thenReturn(AnimeCreator.createAnimeValid());

        //teste7 (diferente quando o retorno é VOID
        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        //teste8 (diferente quando o retorno é VOID
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("creates an anime page and verifies if it is empty or null")
    void test(){
        String expectedName = AnimeCreator.createAnimeValid().getName();

        Page<Anime> animePage = animeController.listPage(null).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("listall returns a list of anime when successful")
    void test2(){
        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> animes = animeController.list().getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by id: returns an anime when successful")
    void test3(){
        Long expectedId = AnimeCreator.createAnimeValid().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Find by name: returns a list of anime when successful")
    void test4(){
        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> animes = animeController.findByName("vai").getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by name: returns an empty list of anime when not found")
    void test5(){

        //comportamento não oficial
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("vai").getBody();

        Assertions.assertThat(animes).isNotNull().isEmpty();


    }

    @Test
    @DisplayName("save return anime when successful")
    void test6(){
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isEqualTo(AnimeCreator.createAnimeValid()).isNotNull();

    }

    @Test
    @DisplayName("replace update return anime when successful")
    void test7(){
        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
        .doesNotThrowAnyException();

        ResponseEntity<Anime> body = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertThat(body).isNotNull();

        Assertions.assertThat(body.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("delete anime when successful")
    void test8(){
        Assertions.assertThatCode(() -> animeController.delete(1)).doesNotThrowAnyException();

        ResponseEntity<Void> body = animeController.delete(1);

        Assertions.assertThat(body).isNotNull();

        Assertions.assertThat(body.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}