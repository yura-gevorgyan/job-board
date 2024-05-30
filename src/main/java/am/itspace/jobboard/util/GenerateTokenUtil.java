package am.itspace.jobboard.util;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateTokenUtil {
    public static String generateToken() {
        return  Integer.toString(ThreadLocalRandom.current().nextInt(100000, 999999));
    }
}
