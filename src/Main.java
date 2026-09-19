import java.awt.*;
import javax.swing.*;

@SuppressWarnings("ResultOfObjectAllocationIgnored")
public final class Main {
    //Vars used to get the screen's width and height, as well as a magnification value for scaling
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static final Dimension screenSize = toolkit.getScreenSize();
    private static final double SCREEN_WIDTH = screenSize.getWidth();
    private static final double SCREEN_HEIGHT = screenSize.getHeight();
    private static double MAGNIFICATION;

    //instatiates screen
    static GUI screen;

    //Main method
    public static void main(String[] args) {
        //sets the magnification to a value that allows the game to fit the screen
        if (SCREEN_HEIGHT/SCREEN_WIDTH < 2560/1440) {
            MAGNIFICATION = SCREEN_WIDTH / 2560;
        }
        else{
            MAGNIFICATION = SCREEN_HEIGHT / 1440;
        }
        screen = new GUI();//creates GUI, displaying start screen
    }

    //scales given image icon based on the screen's magnification
    public static ImageIcon scaleImageIcon(ImageIcon imageIcon){
        //Scales the given imageIcon in accordance with the magnification
        int height = imageIcon.getIconHeight();
        int width = imageIcon.getIconWidth();        
        Image scaledImage = imageIcon.getImage().getScaledInstance((int)(width*MAGNIFICATION), (int)(height*MAGNIFICATION), Image.SCALE_DEFAULT);
        imageIcon.setImage(scaledImage);
        return imageIcon;
    }

    //scales given image icon based on the screen's magnification
    public static ImageIcon resizeImageIcon(ImageIcon imageIcon, int width, int height){ 
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
        imageIcon.setImage(scaledImage);
        return imageIcon;
    }

    //Getter Methods
    public static double getScreenWidth() {
        return SCREEN_WIDTH;
    }
    public static double getScreenHeight() {
        return SCREEN_HEIGHT;
    }
    public static double getMagnification() {
        return MAGNIFICATION;
    }
}