package br.com.magic.application.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static <T> List<T> sortCards(List<T> cards) {
        Random random = new Random();
        List<T> randomCards = new ArrayList<>();

        for (int i = 0; i < 4;) {
            int randomNumber = random.nextInt(9);

            if (!randomCards.contains(cards.get(randomNumber))) {
                randomCards.add(cards.get(randomNumber));
                i++;
            }
        }

        return randomCards;
    }
}
