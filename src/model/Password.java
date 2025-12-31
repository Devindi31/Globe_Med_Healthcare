package model;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Password {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGIT = "0123456789";
    private static final String ALL = UPPER + LOWER + DIGIT;

    private static final SecureRandom RNG = new SecureRandom();

    private static final Set<String> SEEN = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private Password() {
    }

    public static String generateUniquePassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be >= 8");
        }

        while (true) {
            String pwd = buildOnce(length);
            if (SEEN.add(pwd)) {
                return pwd;
            }
        }
    }

    private static String buildOnce(int length) {
        List<Character> chars = new ArrayList<>(length);

        chars.add(randomChar(UPPER));
        chars.add(randomChar(LOWER));
        chars.add(randomChar(DIGIT));

        for (int i = chars.size(); i < length; i++) {
            chars.add(randomChar(ALL));
        }

        Collections.shuffle(chars, RNG);

        StringBuilder sb = new StringBuilder(length);
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static char randomChar(String from) {
        return from.charAt(RNG.nextInt(from.length()));
    }
}
