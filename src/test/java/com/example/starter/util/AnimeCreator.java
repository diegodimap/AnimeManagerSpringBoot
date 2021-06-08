package com.example.starter.util;

import com.example.starter.model.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder().name("VAI LOGO TESTES").build();
    }

    public static Anime createAnimeValid(){
        return Anime.builder().name("VAI LOGO TESTES2").id(1L).build();
    }

    public static Anime createAnimeValidUpdated(){
        return Anime.builder().name("VAI LOGO TESTES 3").id(1L).build();
    }

}
