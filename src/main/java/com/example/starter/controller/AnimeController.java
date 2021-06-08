package com.example.starter.controller;

import com.example.starter.model.Anime;
import com.example.starter.requests.AnimePostRequestBody;
import com.example.starter.requests.AnimePutRequestBody;
import com.example.starter.service.AnimeService;
import com.example.starter.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor //aí não precisa colocar o @Autowired e precisa ser final nas variáveis abaixo
public class AnimeController {

    //@Autowired //inicializa a variável abaixo
    private final DateUtil dateUtil;

    private final AnimeService animeService;

    @GetMapping("/vai")
    public String vai(){
        return "OK";
    }


    @GetMapping("/listpage")
    public ResponseEntity<Page<Anime>> listPage(Pageable pageable){
        //no lugar de só animeService.listAll(), coloca o responseEntity para retornar também um status
        return new ResponseEntity<>(animeService.listAllPages(pageable), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Anime>> list(){

        return new ResponseEntity<>(animeService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        //no lugar de só animeService.listAll(), coloca o responseEntity para retornar também um status
        return new ResponseEntity<>(animeService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/createAnime")
    @ResponseStatus(HttpStatus.CREATED) //outra forma de fazer pra não precisar colocar lá embaixo
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime){ //@Valid vai verificar se o objeto atende aos requisitos de validação
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/replaceAnime")
    public ResponseEntity<Anime> replace(@RequestBody AnimePutRequestBody anime){
        animeService.replace(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/findByName/{name}")
    public ResponseEntity<List<Anime>> findByName(@PathVariable String name){
        //no lugar de só animeService.listAll(), coloca o responseEntity para retornar também um status
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping("/findByName2")
    public ResponseEntity<List<Anime>> findByName2(@RequestParam (required = false, defaultValue = "") String name){
        return ResponseEntity.ok(animeService.findByName(name));
    }
}
