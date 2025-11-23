import javafx.scene.image.Image;

import java.util.Objects;

public class CardImageUtil {

    private static String path = "/cards/";

    public static void setCardTheme(String theme) {
        if (Objects.equals(theme, "classic")) {
            path = "/cards_classic/";
        } else if(Objects.equals(theme, "night")) {
            path = "/cards_night/";
        } else {
            path = "/cards/";
        }
    }

    public static Image loadCard(String rank, String suit) {
        String current = path + rank + "-" + suit + ".png";
        try {
            return new Image(CardImageUtil.class.getResourceAsStream(current));
        } catch (Exception e) {
            System.out.println("Could not find image: " + path);
            return null;
        }
    }

    public static Image loadCardBack() {
        String current = path + "back.png";
        try {
            return new Image(CardImageUtil.class.getResourceAsStream(current));
        } catch (Exception e) {
            return null;
        }
    }
}