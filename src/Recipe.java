public class Recipe extends Page{
   //vars
    private String title;
    private Step[] steps;
    
    //constructors
     public Recipe (GUI screen){ //used when createing a new recipe (recipe with no preset information)
         super (screen); 
     }
     public Recipe(String name, Step[] instructions, GUI screen){ //used when creating a recipe using preexisting information
        super(name, instructions, screen);
        instructions[instructions.length-1].isLastStep(true);
        title = name;
        steps = instructions;
     }

     //getters
     public String getTitle(){
        return title;
     }
     public Step[] getSteps(){
        return steps;
     }
 }
