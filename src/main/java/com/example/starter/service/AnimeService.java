package com.example.starter.service;

//lógica de negócio vai aqui no service

import com.example.starter.exception.BadRequestException;
import com.example.starter.mapper.AnimeMapper;
import com.example.starter.model.Anime;
import com.example.starter.repository.AnimeRepository;
import com.example.starter.requests.AnimePostRequestBody;
import com.example.starter.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }

    public Page<Anime> listAllPages(Pageable pageable){
        return animeRepository.findAll(pageable);
    }

    public Anime findById(long id){
        return animeRepository.findById(id).orElseThrow(() -> new BadRequestException("Anime Not Found")); //antes new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime Not Found"));
    }

    @Transactional(rollbackOn = Exception.class) //se der exceção, não vai salvar no banco
    public Anime save(AnimePostRequestBody animePostRequestBody){
        Anime animeMapeado = AnimeMapper.INSTANCE.toAnime(animePostRequestBody);
        //Anime anime = new Anime().builder().name(animePostRequestBody.getName()).build();

//        if(true){
//            throw new Exception("asdasd");
//        }

        return animeRepository.save(animeMapeado);
    }

    public void delete(long id) {
        animeRepository.delete(findById(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findById(animePutRequestBody.getId()); //ou acha ou vai lançar exceção
        //Anime anime = Anime.builder().id(savedAnime.getId()).name(animePutRequestBody.getName()).build();
        Anime animeMapeado = AnimeMapper.INSTANCE.toAnime(animePutRequestBody);
        animeMapeado.setId(savedAnime.getId());

        animeRepository.save(animeMapeado);
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }
}
