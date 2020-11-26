package at.termftp.backend.dao;

import java.util.Random;

public class TemporaryTokenGeneratorWhichShouldBeReplacedByJWTs {
    private static String chars = "abcdefghijklmnopqrstuvwxyz";

    public static String generate(){
        Random rand = new Random();
        String token = "";
        for (int i = 0; i < 32; i++) {
            token += Character.toString(chars.charAt(rand.nextInt(chars.length())));
        }
        return token;
    }
}
