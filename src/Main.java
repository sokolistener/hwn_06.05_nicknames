import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        AtomicInteger counterFor3 = new AtomicInteger(0);
        AtomicInteger counterFor4 = new AtomicInteger(0);
        AtomicInteger counterFor5 = new AtomicInteger(0);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == 3 && allEqualsTest(texts[i])) {
                    counterFor3.getAndIncrement();
                }
                if (texts[i].length() == 4 && allEqualsTest(texts[i])) {
                    counterFor4.getAndIncrement();
                }
                if (texts[i].length() == 5 && allEqualsTest(texts[i])) {
                    counterFor5.getAndIncrement();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == 3 && palindromTest(texts[i])) {
                    counterFor3.getAndIncrement();
                }
                if (texts[i].length() == 4 && palindromTest(texts[i])) {
                    counterFor4.getAndIncrement();
                }
                if (texts[i].length() == 5 && palindromTest(texts[i])) {
                    counterFor5.getAndIncrement();
                }
            }
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == 3 &&!allEqualsTest(texts[i]) &&incrementTest(texts[i])) {
                    counterFor3.getAndIncrement();
                }
                if (texts[i].length() == 4 &&!allEqualsTest(texts[i]) &&incrementTest(texts[i])) {
                    counterFor4.getAndIncrement();
                }
                if (texts[i].length() == 5 &&!allEqualsTest(texts[i]) &&incrementTest(texts[i])) {
                    counterFor5.getAndIncrement();
                }
            }
        });


        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
        System.out.println("Красивых слов с длиной 3: " + counterFor3.get());
        System.out.println("Красивых слов с длиной 4: " + counterFor4.get());
        System.out.println("Красивых слов с длиной 5: " + counterFor5.get());
    }

    static boolean allEqualsTest(String str) {
//        int counter = 0;
//        for (int i = 0; i < str.length() - 1; i++) {
//            if (str.charAt(i) == str.charAt(i + 1)) {
//                counter++;
//            }
//        }
//        return counter == str.length() - 1;
        return str.replaceAll(str.substring(0, 1), "").equals("");
    }

    static boolean palindromTest(String str) {
        if (str.length() % 2 == 0 && str.substring(0, str.length() / 2).equals(reverseString(str.substring(str.length() / 2)))) {
            return true;
        } else
            return str.length() % 2 == 1 && str.substring(0, str.length() / 2).equals(reverseString(str.substring(str.length() / 2 + 1)));
    }

    static boolean incrementTest(String str) {
        int counter = 0;
        for (int i = 0; i < str.length() - 1; i++) {
            if (Character.getNumericValue(str.charAt(i)) <= Character.getNumericValue(str.charAt(i + 1))) {
                counter++;
            }
        }
        return counter == str.length() - 1;
    }

    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }
}