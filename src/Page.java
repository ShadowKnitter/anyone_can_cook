import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.*;

public class Page extends JPanel{
    private JLabel title = new JLabel();

    private JTextField editTitle;

    private JPanel Left = new JPanel();
    private JPanel Right = new JPanel();

    private JButton close;
    private JButton garbage;

    private ArrayList<Step> addingSteps= new ArrayList<>();

    private static ArrayList<Page> allPages = new ArrayList<>();
    private static int numOfPages = 0;
    private final int ID;

    public Page (GUI screen){
        ID = numOfPages;
        numOfPages++;
        allPages.add(this);

        setBounds(screen.book.getX()+(int)(100*Main.getMagnification()),(int)(570*Main.getMagnification()),(int)(1165*Main.getMagnification()),(int)(800*Main.getMagnification()));
        setOpaque(false);
        setLayout(null);
        setVisible(false);
        screen.layers.add(this,JLayeredPane.MODAL_LAYER);

        Left.setBounds(0,0,getWidth()/2-(int)(40*Main.getMagnification()), getHeight());
        Left.setLayout(new GridLayout(12,1,0,5));
        Left.setOpaque(false);
        add(Left);
        
        Right.setBounds(getWidth()/2+(int)(40*Main.getMagnification()),0,getWidth()/2-(int)(40*Main.getMagnification()), getHeight());
        Right.setLayout(new GridLayout(12,1,0,5));
        Right.setOpaque(false);
        add(Right);

        editTitle = new JTextField("Type Title Here");
        editTitle.setFont(new Font("Elephant", Font.BOLD, (int)(50*(Main.getMagnification()))));
        editTitle.setHorizontalAlignment(JLabel.CENTER);
        editTitle.setForeground(new Color(0,0,50));
        editTitle.setOpaque(false);
        editTitle.setBorder(null);
        editTitle.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (editTitle.getText().equals("Type Title Here")) {
                    editTitle.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (editTitle.getText().isEmpty()) {
                    editTitle.setText("Type Title Here");
                }
            }
        });
        Left.add(editTitle);
        
        JButton getStarted = new JButton("Get Started!");
        getStarted.setHorizontalAlignment(JButton.LEFT);
        getStarted.setBackground(new Color(142,124,94));
        getStarted.setForeground(new Color(0,0,50));
        getStarted.setBorderPainted(false);
        getStarted.setFont(new Font("Elephant", Font.PLAIN, (int)(20*(Main.getMagnification()))));
        getStarted.addActionListener(e -> {
            new Step(this, screen);
            getStarted.setVisible(false);
        });
        Left.add(getStarted);


        garbage = new JButton(Main.resizeImageIcon(new ImageIcon("img/Garbage.png"), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification())));
        garbage.setContentAreaFilled(false);
        garbage.setBorderPainted(false);
        garbage.setBounds((int)(Main.getScreenWidth()-(220*Main.getMagnification())), (int)(Main.getScreenHeight()- (int)(220*Main.getMagnification())), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification()));
        garbage.addActionListener(e -> {
            garbage.setVisible(false);
            screen.turnPageLeft(this);
        });
        screen.layers.add(garbage, JLayeredPane.MODAL_LAYER);

        screen.update();
    }

    public Page(String name, GUI screen){
        ID = numOfPages;
        numOfPages++;
        allPages.add(this);

        setBounds(screen.book.getX()+(int)(100*Main.getMagnification()),(int)(570*Main.getMagnification()),(int)(1165*Main.getMagnification()),(int)(800*Main.getMagnification()));
        setOpaque(false);
        setLayout(null);
        setVisible(false);
        screen.layers.add(this,JLayeredPane.MODAL_LAYER);

        Left.setBounds(0,0,getWidth()/2-(int)(40*Main.getMagnification()), getHeight());
        Left.setLayout(new GridLayout(12,1,0,5));
        Left.setOpaque(false);
        add(Left);
        
        Right.setBounds(getWidth()/2+(int)(40*Main.getMagnification()),0,getWidth()/2-(int)(40*Main.getMagnification()), getHeight());
        Right.setLayout(new GridLayout(12,1,0,5));
        Right.setOpaque(false);
        add(Right);

        title.setText(name);
        title.setFont(new Font("Elephant", Font.BOLD, (int)(25*(Main.getMagnification()))));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setForeground(new Color(0,0,50));
        Left.add(title);

        JLabel spacer = new JLabel();
        spacer.setVisible(false);
        Left.add(spacer);
    }

    public Page(String name,Recipe[] components, GUI screen){
        this(name, screen);
        addNavContent(components, screen);
        close = new JButton(Main.resizeImageIcon(new ImageIcon("img/Book Icon.png"), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification())));
        close.setVisible(false);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.setBounds((int)(Main.getScreenWidth()-(220*Main.getMagnification())), (int)(Main.getScreenHeight()- (int)(220*Main.getMagnification())), (int)(200*Main.getMagnification()), (int)(200*Main.getMagnification()));
        close.addActionListener(e -> {
            close.setVisible(false);
            screen.close();
        });
        screen.layers.add(close, JLayeredPane.MODAL_LAYER);
    }

    public Page(String name, Step[] components, GUI screen){
        this(name, screen);
        addContent(components, screen);
    }

    private void addNavContent(Recipe[] components, GUI screen){
        if (components.length<=22){
            for (int i =0; i<=components.length; i++){
                if (!(components.length == 22 && i ==22)){
                    JButton listItem ;
                    if (components.length<22 && i==components.length){
                        listItem = new JButton("Add Recipe");
                        listItem.addActionListener(e -> {     
                            Recipe create = new Recipe(screen);
                            screen.setWantedID(create.getID());               
                            screen.turnPageRight();
                        });
                    }
                    else{
                        listItem= new JButton(components[i].getTitle());
                        listItem.addActionListener(e -> {
                            for (int j=0; j< components.length; j++){
                                if (e.getSource() == listItem && listItem.getText().equals(components[j].getTitle())){
                                    screen.setWantedID(components[j].getID());
                                    screen.turnPageRight();
                                }
                            }
                        });
                    }
                    if(i<10){
                        Left.add(listItem);
                    }
                    else{
                        Right.add(listItem);
                    }
                    listItem.setHorizontalAlignment(JButton.LEFT);
                    listItem.setBackground(new Color(142,124,94));
                    listItem.setForeground(new Color(0,0,50));
                    listItem.setBorderPainted(false);
                    listItem.setFont(new Font("Elephant", Font.PLAIN, (int)(20*(Main.getMagnification()))));
                }
            }
        }
    }

    private void addContent(Step[] components,GUI screen){
        if (components.length<=22){
            for (int i =0; i<=components.length; i++){
                JButton listItem;
                if(i==components.length){
                    listItem = new JButton("Return");
                    listItem.addActionListener(e -> {                    
                        screen.setWantedID(screen.getRecipesPageID());
                        if (Step.getCurrentlyDisplayedId() != -1){
                            Step.getWithID(Step.getCurrentlyDisplayedId()).setVisible(false);
                            Step.setCurrentlyDisplayedId(-1);
                        }
                        screen.turnPageLeft(this);
                    });
                }
                else{
                    listItem = new JButton(components[i].getText());
                    listItem.addActionListener(e -> {                    
                        for (int j=0; j< components.length; j++){
                            if (e.getSource() == listItem && listItem.getText().equals(components[j].getText())){
                                if (Step.getCurrentlyDisplayedId() != -1){
                                    Step.getWithID(Step.getCurrentlyDisplayedId()).setVisible(false);
                                }
                                components[j].setVisible(true);
                                Step.setCurrentlyDisplayedId(components[j].getID());
                            }
                        }
                    });
                }
                if(i<10){
                    Left.add(listItem);
                }
                    else{
                    Right.add(listItem);
                }
                listItem.setHorizontalAlignment(JButton.LEFT);
                listItem.setBackground(new Color(142,124,94));
                listItem.setForeground(new Color(0,0,50));
                listItem.setBorderPainted(false);
                listItem.setFont(new Font("Elephant", Font.PLAIN, (int)(20*(Main.getMagnification()))));
            }
        }
    }

    public final void setCloseVisible(boolean isVisible){
        close.setVisible(isVisible);
    }
    public final void setGarbageVisible(boolean isVisible){
        garbage.setVisible(isVisible);
    }

    public final int getID(){
        return ID;
    }

    public static Page getWithID(int id){
        return allPages.get(id);
    }

    public static int getNumOfPages(){
        return numOfPages;
    }

    public ArrayList<Step> getAddingSteps(){
        return addingSteps;
    }

    public void addStep(Step step, GUI screen){
        if (Right.getComponentCount()<11){
            addingSteps.add(step);
            JButton listItem = new JButton(step.getText());
            if(Left.getComponentCount()<12){
                Left.add(listItem);
                listItem.addActionListener(e -> {                    
                    Left.remove(listItem);
                    addingSteps.remove(step);
                    screen.update();
                });
            }
            else{
                Right.add(listItem);
                listItem.addActionListener(e -> {                    
                    Right.remove(listItem);
                    addingSteps.remove(step);
                    screen.update();
                });
            }
            listItem.setHorizontalAlignment(JButton.LEFT);
            listItem.setBackground(new Color(142,124,94));
            listItem.setForeground(new Color(0,0,50));
            listItem.setBorderPainted(false);
            listItem.setFont(new Font("Elephant", Font.PLAIN, (int)(20*(Main.getMagnification()))));
        }
    }

    public void savePage(GUI screen){
        if (screen.getRecipes().length<=22){
            Recipe[] tempRecipes = new Recipe[screen.getRecipes().length+1];
            for (int i = 0; i<screen.getRecipes().length; i++){
                tempRecipes[i] =screen.getRecipes()[i];
            }
            Step[] tempSteps = new Step[addingSteps.size()];
            for (int i = 0; i<addingSteps.size(); i++){
                tempSteps[i] = new Step(addingSteps.get(i).getIconNum(),addingSteps.get(i).getText(), screen);
            }
            
            tempRecipes[screen.getRecipes().length] = new Recipe(editTitle.getText(), tempSteps, screen);

            screen.setRecipes(tempRecipes);
            screen.recipesPage = new Page ("Recipes", screen.getRecipes(), screen);
            screen.turnPageLeft(this);

            try {
                //deletes the outdated information
                File file = new File("saveData.txt");
                file.delete();
                try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
                    pw.println("\n"+screen.getTitle());
                    pw.print(screen.getRecipes().length);
                    for (int i = 0; i<screen.getRecipes().length; i++) {
                        Recipe recipe = screen.getRecipes()[i];
                        pw.println("\n\n"+recipe.getSteps().length);

                        for (int j = 0; j<recipe.getSteps().length; j++){
                            pw.println(recipe.getSteps()[j].getIconNum());
                            pw.println(recipe.getSteps()[j].getText());
                        }
                        pw.print(recipe.getTitle());
                    }
                }
            } 
            catch (IOException e){
                System.out.println("ERROR AT CATCH WHEN SAVING: " + e);
            }
        }
    }

}
