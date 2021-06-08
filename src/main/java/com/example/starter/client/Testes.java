package com.example.starter.client;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Testes {

    public static void main(String[] args) {
        List<String> letters = Arrays.asList("a", "b", "c");

        List<String> upperCase = letters.stream()
                .map((String element) -> element.toUpperCase())
                .collect(Collectors.toList());

        System.out.println(upperCase);

        //OR

        List<String> upperCase2 = letters.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(upperCase2);


        //lista de listas vira uma lista só de elementos FLAT MAP
        List<String> letters1 = Arrays.asList("a", "b");
        List<String> letters2 = Arrays.asList("b", "c", "d");
        List<String> letters3 = Arrays.asList("e", "f");
        List<List<String>> listOfLetters = Arrays.asList(letters1, letters2, letters3);

        List<String> flatList = listOfLetters.stream()
                .flatMap(List::stream)                   //FLAT MAP
                .collect(Collectors.toList());
        System.out.println(flatList);

        //OR with arrays FLATMAP
        String[] animals = new String[]{"cat", "dog", "rabbit"};
        String[] birds = new String[]{"sparrow", "crow", "eagle"};
        String[] fish = new String[]{"cod", "tuna", "salmon"};

        String[][] creatures = new String[][]{animals, birds, fish};

        String[] flatCreatures = Arrays.stream(creatures)
                .flatMap(Arrays::stream)
                .toArray(String[]::new);
        for (int i=0; i<flatCreatures.length; i++) {
            System.out.print(flatCreatures[i] + ", ");
        }

    }
}
