package at.termftp.backend.dao;

import java.util.Random;

/**
 * This class provides some nice error messages
 */
public class ErrorMessages {

    public static String[] invalidPassword = {
            "Invalid Password",
            "Your password seems to be incorrect",
            "There seems to be something wrong with your password",
            "You need to use a valid password!",
            "Something's wrong with your password.",
            "Your password is not correct. Please try another one.",
            "Hmmm, that's not the right password!"
    };
    public static String[] invalidUserName = {
            "Hmmm, this username seems to be invalid. Please try a real one",
            "This user does not exist!",
            "You need to enter a real username!!"
    };
    public static String[] invalidUserID = {
            "This user does not exist, my friend!",
            "Something's wrong with this user_id. Please try another one."
    };

    public static String[] duplicateEmail = {
            "This email does already exist!"
    };
    public static String[] duplicateUsername = {
            "This username does already exist!"
    };


    public static String getInvalidPassword(){
        Random rand = new Random();
        return invalidPassword[rand.nextInt(invalidPassword.length)];
    }
    public static String getInvalidUsername(){
        Random rand = new Random();
        return invalidUserName[rand.nextInt(invalidUserName.length)];
    }
    public static String getInvalidUserID(){
        Random rand = new Random();
        return invalidUserID[rand.nextInt(invalidUserID.length)];
    }
    public static String getDuplicateEmail(){
        Random rand = new Random();
        return duplicateEmail[rand.nextInt(duplicateEmail.length)];
    }
    public static String getDuplicateUsername(){
        Random rand = new Random();
        return duplicateUsername[rand.nextInt(duplicateUsername.length)];
    }

}
