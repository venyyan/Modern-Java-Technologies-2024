ublic class BrokenKeyboard {
    public static int calculateFullyTypedWords(String message, String brokenKeys){
        String[] words = message.split(" ");
 
        int wordsCount = 0;
 
        for (int i = 0; i < words.length; i++) {
            boolean foundBrokenWord = false;
            for (int j = 0; j < brokenKeys.length(); j++) {
                if (words[i].contains(String.valueOf(brokenKeys.charAt(j)))) {
                    foundBrokenWord = true;
                    break;
                }
            }
            if (!foundBrokenWord && !words[i].isBlank())
                wordsCount++;
        }
        return wordsCount;
    }
}
