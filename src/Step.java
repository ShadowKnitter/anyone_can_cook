import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Step extends JPanel{
    //content
    private String text;
    private JButton nextStep;
    private int iconNum = -1;

    //identification and images
    private final int ID;
    private static ArrayList<Step> allSteps = new ArrayList<>();
    private static int numOfSteps = 0;
    private static int CurrentlyDisplayedId = -1;
    private static final ImageIcon[] Icons = new ImageIcon[]{new ImageIcon("img/Mixer.png"),new ImageIcon("img/Bowl.png"),new ImageIcon("img/Blender.png"),new ImageIcon("img/Oven.png"),new ImageIcon("img/Knife.png"),new ImageIcon("img/Stove.png"),new ImageIcon("img/Pan.png"),};

    //constructors
    public Step(Page addTo, GUI screen){ //used to create a step without data
        //resizes all the icons
        for (int i = 0; i< Step.Icons.length; i++){
            Step.Icons[i] = Main.resizeImageIcon(Step.Icons[i], (int)(410*(Main.getMagnification())), (int)(410*(Main.getMagnification())));
        }

        //sets ID and saves to static array of all existing steps
        ID = numOfSteps;
        numOfSteps++;
        allSteps.add(this);

        //panel setup
        setBounds(0,0,(int)Main.getScreenWidth(),(int)(450*Main.getMagnification()));
        setBackground(new Color(235,203,147));
        setOpaque(true);
        setLayout(null);

        //icon selector setup
        JButton icon = new JButton("Click to change icon");
        icon.setBounds((int)(20*Main.getMagnification()),(int)(20*Main.getMagnification()),(int)(410*Main.getMagnification()),(int)(410*Main.getMagnification()));
        icon.setContentAreaFilled(false);
        icon.setBorderPainted(false);
        icon.addActionListener(e -> { //switchs icon the the next one in the array when the button is pressed
            icon.setText("");
            iconNum++;
            if (iconNum>= Step.Icons.length){
                iconNum = 0;
            }
            icon.setIcon(Step.Icons[iconNum]);
        });
        add(icon);

        //instruction editor setup
        JTextField instructions = new JTextField(" Type step instuctions here");
        instructions.setFont(new Font("Elephant", Font.PLAIN, (int)(100*(Main.getMagnification()))));
        instructions.setBackground(new Color(142,124,94));
        instructions.setForeground(new Color(0,0,50));
        instructions.setBorder(null);
        instructions.setBounds((int)(450*Main.getMagnification()),(int)(25*Main.getMagnification()),(int)(1800*Main.getMagnification()),(int)(400*Main.getMagnification()));
        instructions.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) { //clears text and sets step number into text field when it is clicked
                if (instructions.getText().equals(" Type step instuctions here") || instructions.getText().equals(" You must type step instuctions here")) {
                    instructions.setText(" "+(addTo.getAddingSteps().size()+1)+". ");
                }
            }

            @Override
            public void focusLost(FocusEvent e) { //replaces instructions when user doesnt input anything, or inputs something invalid
                if (instructions.getText().isEmpty() || instructions.getText().equals(" ") || instructions.getText().equals(" "+(addTo.getAddingSteps().size()+1)+". ")) {
                    instructions.setText(" Type step instuctions here");
                }
            }
        });
        add(instructions);

        //step addition button setup
        JButton addStep = new JButton(Main.resizeImageIcon(new ImageIcon("img/Add Button.png"), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification())));
        addStep.setBounds((int)(2300*Main.getMagnification()),(int)(17*Main.getMagnification()),(int)(200*Main.getMagnification()),(int)(200*Main.getMagnification()));
        addStep.setContentAreaFilled(false);
        addStep.setBorderPainted(false);
        addStep.addActionListener(e -> { //ensures step isnt empty, and adds it to the recipe if it is not
            if (instructions.getText().isEmpty() || instructions.getText().equals(" Type step instuctions here") || instructions.getText().equals(" You must type step instuctions here")){
                instructions.setText(" You must type step instuctions here");
            }
            else{ //adds the step to the recipe
                addTo.addStep(new Step(iconNum, instructions.getText(), screen), screen);
                setVisible(false);
                new Step(addTo, screen);
            }
        });
        add(addStep);

        //finished recipe button setup
        JButton finished = new JButton(Main.resizeImageIcon(new ImageIcon("img/Check Button.png"), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification())));
        finished.setBounds((int)(2300*Main.getMagnification()),(int)(234*Main.getMagnification()),(int)(200*Main.getMagnification()),(int)(200*Main.getMagnification()));
        finished.setContentAreaFilled(false);
        finished.setBorderPainted(false);
        finished.addActionListener(e -> {//ensures at least one step has been added, and adds the recipe to the recipes list if it is valid
            if (!addTo.getAddingSteps().isEmpty()){
                setVisible(false);
                addTo.setGarbageVisible(false);
                addTo.savePage(screen);
            }
        });
        add(finished);
        
        Step.setCurrentlyDisplayedId(ID); //sets this step editor as currently displayed
        addTo.requestFocus();
        screen.layers.add(this, JLayeredPane.MODAL_LAYER);
        screen.update();
    }
    public Step (int iconN, String txt, GUI screen){ //used to create a step with existing data
        //sets the icon number
        iconNum = iconN;

        //sets ID and saves to static array of all existing steps
        ID = numOfSteps;
        numOfSteps++;
        allSteps.add(this);

        //panel setup

        setBounds(0,0,(int)Main.getScreenWidth(),(int)(450*Main.getMagnification()));
        setBackground(new Color(235,203,147));
        setOpaque(true);
        setVisible(false);
        setLayout(null);

        //icon setup and display
        if (iconNum>=0){
            JLabel icon = new JLabel(Main.resizeImageIcon(Step.Icons[iconNum], (int)(410*(Main.getMagnification())), (int)(410*(Main.getMagnification()))));
            icon.setBounds((int)(20*Main.getMagnification()),(int)(20*Main.getMagnification()),(int)(410*Main.getMagnification()),(int)(410*Main.getMagnification()));
            icon.setOpaque(false);
            add(icon);
        }

        //instructions setup and display
        text = txt;
        JLabel instructions = new JLabel("<html>"+text+"<html>");
        instructions.setFont(new Font("Elephant", Font.PLAIN, (int)(100*(Main.getMagnification()))));
        instructions.setBackground(new Color(142,124,94));
        instructions.setForeground(new Color(0,0,50));
        instructions.setOpaque(true);
        instructions.setBounds((int)(450*Main.getMagnification()),(int)(25*Main.getMagnification()),(int)(1800*Main.getMagnification()),(int)(400*Main.getMagnification()));
        add(instructions);

        //next step button setup
        nextStep = new JButton(Main.resizeImageIcon(new ImageIcon("img/Check Button.png"), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification())));
        nextStep.setBounds((int)(2300*Main.getMagnification()),(int)(125*Main.getMagnification()),(int)(200*Main.getMagnification()),(int)(200*Main.getMagnification()));
        nextStep.setContentAreaFilled(false);
        nextStep.setBorderPainted(false);        
        nextStep.addActionListener(e -> {//shows the next step in the recipe
            setVisible(false);
            Step.getWithID(ID+1).setVisible(true);
            Step.setCurrentlyDisplayedId(ID+1);
        });
        add(nextStep);

        screen.layers.add(this, JLayeredPane.MODAL_LAYER);
    }

    //getters
    public int getIconNum(){
        return iconNum;
    }
    public String getText(){
        return text;
    }
    public int getID (){
        return ID;
    }
    public static int getCurrentlyDisplayedId(){
        return CurrentlyDisplayedId;
    }
    public static Step getWithID(int id){
        return allSteps.get(id);
    }

    //setters
    public static void setCurrentlyDisplayedId(int id){
        CurrentlyDisplayedId = id;
    }

    //updates and accounts for this being the last step in a recipe
    public void isLastStep(boolean last){
        nextStep.setVisible(!last);
    }
}