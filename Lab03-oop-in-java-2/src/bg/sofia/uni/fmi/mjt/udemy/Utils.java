package bg.sofia.uni.fmi.mjt.udemy;

public class Utils {
    public static boolean containsOnlyLetters(String str) {
        for (int i = 0; i < str.length(); i++) {
            char currChar = str.charAt(i);
            if (!Character.isLetter(currChar)) {
                return false;
            }
        }
        return true;
    }
}
