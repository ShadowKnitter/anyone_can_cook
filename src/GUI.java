import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public final class GUI extends JFrame implements ActionListener{
    //component vars
    public JLayeredPane layers;
    public MonitoringLabel book;
    private JLabel table = new JLabel();
    private JTextArea title = new JTextArea();
    private JButton openButton = new JButton("Lets Cook!");

    //Vars used for tracking pages and recipes
    private Recipe [] recipes;
    private int wantedID;
    public Page recipesPage;

    //Image and aesthetic variables
    private ImageIcon bookIcon = new ImageIcon("img/book.png");
    private final int bookWidth = (int)(bookIcon.getIconWidth()*3*Main.getMagnification());
    private final int bookHeight = (int)(bookIcon.getIconHeight()*3*Main.getMagnification());

    //constructor
    public GUI(){
        //used with the monitoring label to detect when gifs finish, used for page turn animations
        ActionListener animationListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("stopped")){
                    //closes program if there is no valid id, prevents any potential crahes and allows the book to close.
                    if (!(wantedID >= 0)){
                        dispose();
                    }
                    else{
                        //shows the requested page
                        for (int i=0; i< Page.getNumOfPages(); i++){
                            if (i==wantedID){
                                Page.getWithID(i).setVisible(true);
                                if (wantedID == recipesPage.getID()){
                                    recipesPage.setCloseVisible(true);
                                }
                            }
                        }
                    }
                }
            }
        };

        // creates and sets up the game's window/Jframe
        setTitle("Anyone CAN COOK!");
        setIconImage(new ImageIcon("img/Book Icon.png").getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setBounds(0,0, (int)Main.getScreenWidth(),(int)Main.getScreenHeight());
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(0, 0, 0));
        setLayout(null);
        setVisible(true);

        //layer setup
        layers = new JLayeredPane();
        layers.setBounds(0,0, getWidth(), getHeight());
        add(layers);

        //background setup
        table.setIcon(Main.resizeImageIcon(new ImageIcon("img/Table BG.png"),(int)(Main.getScreenWidth()), (int)(Main.getScreenHeight())));
        table.setBounds(0,0, (int)Main.getScreenWidth(), (int)Main.getScreenHeight());
        layers.add(table, JLayeredPane.FRAME_CONTENT_LAYER);
        
        //book art setup
        book = new MonitoringLabel(Main.resizeImageIcon(bookIcon, bookWidth, bookHeight));
        book.setBounds((int)((Main.getScreenWidth()-bookWidth)/2),(int)((Main.getScreenHeight()-bookHeight*1.05)/2),bookWidth, bookHeight);
        book.addActionListener(animationListener);
        layers.add(book, JLayeredPane.PALETTE_LAYER);

        //book opening button setup
        openButton.addActionListener(this);
        openButton.setBounds((int)(book.getX()+750*Main.getMagnification()),(int)(book.getY()+1100*Main.getMagnification()),(int)(500*Main.getMagnification()),(int)(100*Main.getMagnification()));
        openButton.setContentAreaFilled(false);
        openButton.setBorderPainted(false);
        openButton.setFont(new Font("Elephant", Font.BOLD, (int)(50*(Main.getMagnification()))));
        openButton.setForeground(new Color(0,0,50));
        layers.add(openButton, JLayeredPane.MODAL_LAYER);

        //title setup
        title.setText("Anyone CAN COOK!");
        title.setBounds((int)(book.getX()+700*Main.getMagnification()),(int)(book.getY()+650*Main.getMagnification()),(int)(600*Main.getMagnification()),(int)(400*Main.getMagnification()));
        title.setFont(new Font("Elephant", Font.BOLD, (int)(100*(Main.getMagnification()))));
        title.setLineWrap(true);
        title.setWrapStyleWord(true);
        title.setOpaque(false);
        title.setForeground(new Color(0,0,50));
        title.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (title.getText().equals("Anyone CAN COOK!")) {
                    title.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (title.getText().isEmpty()) {
                    title.setText("Anyone CAN COOK!");
                }
            }
        });
        layers.add(title, JLayeredPane.MODAL_LAYER);

        update(); //updates screen, adding all elements

        //sets the free recipe as the only one in case there is no save data, and then loads to replace the data if there is any saved
        recipes = new Recipe[]{new Recipe("Cherry-Banana Smoothie", new Step[]{new Step(2, " 1. Cut up 1 banana and de-pit 300 grams of black forest cherries.",this),new Step(2, " 2. Place the prepared ingrediants into a blender.",this),new Step(2, " 3. Add 185 grams of plain yogurt and 100 grams of blueberry yogurt.",this), new Step(2, " 4. Pour in 500 ml of mango juice.",this),new Step(2, " 5. Progressively blend for 5 minutes.",this), new Step(2, " 6. Enjoy!",this)}, this)};
        load();

        //creates the recipes/navigation page
        recipesPage = new Page ("Recipes", recipes, this);

        //waits 4 seconds before indicating the user can chose their own title, if they have not already
        try {
            Thread.sleep(4000);
            if (title.getText().equals("Anyone CAN COOK!")){
                title.requestFocus();
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR AT SLEEP: " + e);
        }
    }

    //preforms when the open button is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            //sets the JFrames title and flips to the recipes/navigation page
            setTitle(title.getText());
            wantedID = recipesPage.getID();
            book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Opening.gif"), bookWidth, bookHeight));
            openButton.setVisible(false);
            title.setVisible(false);
            playPageTurn();
        }
    }

    //closes the program
    public final void close(){
        wantedID = -1;
        recipesPage.setVisible(false);
        recipesPage.setCloseVisible(false);
        book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Closing.gif"), bookWidth, bookHeight));
        playPageTurn();
    }

    //turns the page right, used when moving to a menu from the recipes/navigation page
    public final void turnPageRight(){
        recipesPage.setVisible(false);
        recipesPage.setCloseVisible(false);
        book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Page Turn Right.gif"), bookWidth, bookHeight));
        playPageTurn();
    }
    //turns the page left, used when moving from a menu to the recipes/navigation page
    public final void turnPageLeft(Page fromPage){
        fromPage.setVisible(false);
        wantedID = recipesPage.getID();
        book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Page Turn Left.gif"), bookWidth, bookHeight));
        playPageTurn();
    }

    //plays a page turning sound effect once
    public final void playPageTurn(){
        try {
            File audioFile = new File("Page turn.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip sfx = AudioSystem.getClip();
            sfx.open(audioStream);
            sfx.setFramePosition(0); 
            sfx.start();
        }  
        catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported Audio File Exception AT TRY CATCH OF MUSIC SETUP IN MAIN: " + e);
        }
        catch (IOException e) {
            System.out.println("IO Exception AT TRY CATCH OF MUSIC SETUP IN MAIN: " + e);
        }
        catch (LineUnavailableException e) {
            System.out.println("Line Unavailable Exception AT TRY CATCH OF MUSIC SETUP IN MAIN: " + e);
        }
    }

    //setters
    public final void setWantedID(int id){
        wantedID = id;
    }
    public final void setRecipes(Recipe[] list){
        recipes = list;
    }

    //getters
    public final int getRecipesPageID(){
        return recipesPage.getID();
    }
    public final Recipe[] getRecipes(){
        return recipes;
    }
    

    //repaints and revalidates the screen
    public final void update(){
        revalidate();
        repaint();
    }   
    
    //loads save data
    private final void load(){
        try (BufferedReader br = new BufferedReader(new FileReader("saveData.txt"));){
            if(br.readLine()!=null){ //ensures the save data file is not empty
                title.setText(br.readLine()); //sets the cookbook title
                
                recipes = new Recipe[Integer.parseInt(br.readLine())]; //sets the recipe array to the required size
                for(int i = 0; i <recipes.length; i++){ //repeats for each spot in the array
                    br.readLine();
                    Step [] loadSteps = new Step[Integer.parseInt(br.readLine())];//creates a step array if the required size
                    for (int j = 0; j<loadSteps.length; j++){ //fills the step array
                        Step step = new Step(Integer.parseInt(br.readLine()), br.readLine(), this); //creates the saved step
                        loadSteps[j] = step;
                    }
                    Recipe recipe = new Recipe(br.readLine(), loadSteps, this); //creates the saved recipe
                    recipes[i] = recipe;
                }
            }
        } 
        catch (IOException e) {
            System.out.println("ERROR AT CATCH WHEN LOADING: " + e);
        }
    }
}
