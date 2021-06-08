package com.example.starter.util;

import com.example.starter.requests.AnimePostRequestBody;
import com.example.starter.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createAnimePutRequestBody(){
        return AnimePutRequestBody.builder().name(AnimeCreator.createAnimeToBeSaved().getName()).build();
    }
}
