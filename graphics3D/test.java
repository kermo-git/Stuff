package graphics3D;

public class Test {
    public static void main(String[] args) {
        String emojis = "🧎‍♀️🧎🩲";
        System.out.println("Characters in " + emojis + ":");
        for (int i = 0; i < emojis.length(); i++) {
            System.out.println(emojis.charAt(i));
        }
    }
}
