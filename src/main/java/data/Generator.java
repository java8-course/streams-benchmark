package data;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class Generator {
    private Generator() {
    }

    private static String generateString() {
        final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final int maxLength = 10;
        final int length = ThreadLocalRandom.current().nextInt(maxLength) + 1;

        return IntStream.range(0, length)
                .mapToObj(letters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private static String[] generateStringArray(int length) {
        return Stream.generate(Generator::generateString)
                .limit(length * 10)
                .distinct()
                .limit(length)
                .toArray(String[]::new);
    }

    private static String pickString(String[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    public static List<Pair> generatePairs(int idCount, int length) {
        final String[] ids = generateStringArray(idCount);

        return Stream.generate(() -> new Pair(new Key(pickString(ids)), new Value(pickString(ids))))
                .limit(length)
                .collect(toList());
    }

}
