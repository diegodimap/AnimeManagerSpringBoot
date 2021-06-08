package com.example.starter.service;

import com.example.starter.model.Anime;
import com.example.starter.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks //classe principal
    private AnimeService animeService;

    @Mock //classes dentro da principal
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createAnimeValid()));
        //um comportamento para cada teste

        //teste1
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);

        //teste2
        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(List.of(AnimeCreator.createAnimeValid()));

        //teste3
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(AnimeCreator.createAnimeValid()));

        //teste4
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(List.of(AnimeCreator.createAnimeValid()));

        //teste6
        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class))).thenReturn(AnimeCreator.createAnimeValid());

        //teste7 (diferente quando o retorno é VOID
        //BDDMockito.doNothing().when(animeRepositoryMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        //teste8 (diferente quando o retorno é VOID
        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("creates an anime page and verifies if it is empty or null")
    void test(){
        String expectedName = AnimeCreator.createAnimeValid().getName();

        Page<Anime> animePage = animeService.listAllPages(PageRequest.of(1,1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList()).isNotEmpty();
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("listall returns a list of anime when successful")
    void test2(){
        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> animes = animeService.listAll();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by id: returns an anime when successful")
    void test3(){
        Long expectedId = AnimeCreator.createAnimeValid().getId();

        Anime anime = animeService.findById(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Find by name: returns a list of anime when successful")
    void test4(){
        String expectedName = AnimeCreator.createAnimeValid().getName();

        List<Anime> animes = animeService.findByName("vai");

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find by name: returns an empty list of anime when not found")
    void test5(){

        //comportamento não oficial
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName("vai");

        Assertions.assertThat(animes).isNotNull().isEmpty();


    }

    @Test
    @DisplayName("save return anime when successful")
    void test6(){
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());

        Assertions.assertThat(anime).isEqualTo(AnimeCreator.createAnimeValid()).isNotNull();

    }

    @Test
    @DisplayName("replace update return anime when successful")
    void test7(){
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();


    }

    @Test
    @DisplayName("delete anime when successful")
    void test8(){
        Assertions.assertThatCode(() -> animeService.delete(1)).doesNotThrowAnyException();


    }
}