package co.edu.uniquindio.unieventos.util;

public class utilitaryClass {

    public static String generateCode(int size) {
        String string = "ABCOEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * string.length());
            char character = string.charAt(index);

            result.append(character);
        }
        return result.toString();
    }

}
