import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public final class GUI extends JFrame implements ActionListener{
    public JLayeredPane layers;
    public MonitoringLabel book;

    private JLabel table = new JLabel();
    private JTextArea title = new JTextArea();
  
    private JButton openButton = new JButton("Lets Cook!");

    private Recipe [] recipes;
    private int wantedID;
    public Page recipesPage;

    private ImageIcon bookIcon = new ImageIcon("img/book.png");
    private int bookWidth = (int)(bookIcon.getIconWidth()*3*Main.getMagnification());
    private int bookHeight = (int)(bookIcon.getIconHeight()*3*Main.getMagnification());

    public GUI(){

        ActionListener animationListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("stopped")){
                    if (wantedID == -1){
                        dispose();
                    }
                    else{
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

        layers = new JLayeredPane();
        layers.setBounds(0,0, getWidth(), getHeight());
        add(layers);

        table.setIcon(Main.resizeImageIcon(new ImageIcon("img/Table BG.png"),(int)(Main.getScreenWidth()), (int)(Main.getScreenHeight())));
        table.setBounds(0,0, (int)Main.getScreenWidth(), (int)Main.getScreenHeight());
        layers.add(table, JLayeredPane.FRAME_CONTENT_LAYER);
        

        book = new MonitoringLabel(Main.resizeImageIcon(bookIcon, bookWidth, bookHeight));
        book.setBounds((int)((Main.getScreenWidth()-bookWidth)/2),(int)((Main.getScreenHeight()-bookHeight*1.05)/2),bookWidth, bookHeight);
        book.addActionListener(animationListener);
        layers.add(book, JLayeredPane.PALETTE_LAYER);

        openButton.addActionListener(this);
        openButton.setBounds((int)(book.getX()+750*Main.getMagnification()),(int)(book.getY()+1100*Main.getMagnification()),(int)(500*Main.getMagnification()),(int)(100*Main.getMagnification()));
        openButton.setContentAreaFilled(false);
        openButton.setBorderPainted(false);
        openButton.setFont(new Font("Elephant", Font.BOLD, (int)(50*(Main.getMagnification()))));
        openButton.setForeground(new Color(0,0,50));
        layers.add(openButton, JLayeredPane.MODAL_LAYER);

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

        update();

        recipes = new Recipe[]{new Recipe("Cherry-Banana Smoothie", new Step[]{new Step(2, " 1. Cut up 1 banana and de-pit 300 grams of black forest cherries.",this),new Step(2, " 2. Place the prepared ingrediants into a blender.",this),new Step(2, " 3. Add 185 grams of plain yogurt and 100 grams of blueberry yogurt.",this), new Step(2, " 4. Pour in 500 ml of mango juice.",this),new Step(2, " 5. Progressively blend for 5 minutes.",this), new Step(2, " 6. Enjoy!",this)}, this)};
        load();

        recipesPage = new Page ("Recipes", recipes, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            setTitle(title.getText());
            wantedID = recipesPage.getID();
            book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Opening.gif"), bookWidth, bookHeight));
            openButton.setVisible(false);
            title.setVisible(false);
        }
    }

    public final void close(){
        wantedID = -1;
        recipesPage.setVisible(false);
        recipesPage.setCloseVisible(false);
        book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Closing.gif"), bookWidth, bookHeight));
    }

    public final void turnPageRight(){
        recipesPage.setVisible(false);
        recipesPage.setCloseVisible(false);
        book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Page Turn Right.gif"), bookWidth, bookHeight));
    }
    public final void turnPageLeft(Page fromPage){
        fromPage.setVisible(false);
        wantedID = recipesPage.getID();
        book.setIcon(Main.resizeImageIcon(new ImageIcon("img/Book Page Turn Left.gif"), bookWidth, bookHeight));
    }
    public final void setWantedID(int id){
        wantedID = id;
    }
    public final int getRecipesPageID(){
        return recipesPage.getID();
    }

    public final Recipe[] getRecipes(){
        return recipes;
    }
    public final void setRecipes(Recipe[] list){
        recipes = list;
    }

    //repaints and revalidates the screen
    public final void update(){
        revalidate();
        repaint();
    }   
    
    private final void load(){
        try (BufferedReader br = new BufferedReader(new FileReader("saveData.txt"));){
            if(br.readLine()!=null){ //ensures the save data file is not empty
                title.setText(br.readLine());
                
                recipes = new Recipe[Integer.parseInt(br.readLine())];
                for(int i = 0; i <recipes.length; i++){
                    br.readLine();
                    Step [] loadSteps = new Step[Integer.parseInt(br.readLine())];
                    for (int j = 0; j<loadSteps.length; j++){
                        Step step = new Step(Integer.parseInt(br.readLine()), br.readLine(), this);
                        loadSteps[j] = step;
                    }
                    Recipe recipe = new Recipe(br.readLine(), loadSteps, this);
                    recipes[i] = recipe;
                }
            }
        } 
        catch (IOException e) {
            System.out.println("ERROR AT CATCH WHEN LOADING: " + e);
        }
    }
}
