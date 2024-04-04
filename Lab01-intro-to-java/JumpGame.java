public class JumpGame {
    public static boolean canWin(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == 0 && i == 0)
                return false;

            if (array[i] == 0) {
                int zerosCounter = 1;
                int j = i + 1;
                while (j < array.length && array[j] == 0){
                    zerosCounter++;
                    j++;
                }

                boolean isFound = false;
                for (int k = i - 1; k >= 0; k--){
                    if (array[k] >= zerosCounter + i - 1 - k) {
                        i += zerosCounter;
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    return false;
                }
            } else {
               if (array[i] >= array.length - i + 1)
                   return true;
            }
        }
        return true;
    }
}
