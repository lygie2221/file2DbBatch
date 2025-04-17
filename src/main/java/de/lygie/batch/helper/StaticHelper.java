package de.lygie.batch.helper;

import java.math.BigInteger;
import java.util.Random;

public class StaticHelper {

    private static final Random rnd = new Random();

    public static String randomString(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static int randomInt(int length) {

        if (length > 9){
            length=9;
        }
        return (new Random()).nextInt((9 * (int) Math.pow(10, length - 1)) - 1)
                + (int) Math.pow(10, length - 1);
    }

    /**
     * Liefert eine zufällige Dezimalzahl mit genau {@code length} Stellen.
     * Für length ≤ 9 wird int/long verwendet, darüber BigInteger.
     */
    public static BigInteger randomDecimal(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be > 0");
        }

        // für bis zu 9 Stellen kann man bequem int nutzen
        if (length <= 9) {
            int min = (int) Math.pow(10, length - 1);
            int maxExclusive = (int) Math.pow(10, length);
            int val = rnd.nextInt(maxExclusive - min) + min;
            return BigInteger.valueOf(val);
        }

        // für große Längen: BigInteger-Bereich [10^(length-1), 10^length - 1]
        BigInteger min = BigInteger.TEN.pow(length - 1);
        BigInteger range = BigInteger.TEN.pow(length).subtract(min);  // = 10^length - 10^(length-1)

        BigInteger randInRange;
        do {
            // Erzeuge eine zufällige BigInteger mit ausreichend vielen Bits
            randInRange = new BigInteger(range.bitLength(), rnd);
            // wiederholen, falls randInRange ≥ range
        } while (randInRange.compareTo(range) >= 0);

        // schiebe in den Bereich [min, min+range-1] = [10^(length-1), 10^length-1]
        return randInRange.add(min);
    }

}

