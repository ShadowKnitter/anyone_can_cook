public class Recipe extends Page{
    private String title;
    private Step[] steps;
    
     public Recipe (GUI screen){
         super (screen);
     }

     public Recipe(String name, Step[] instructions, GUI screen){
        super(name, instructions, screen);
        instructions[instructions.length-1].isLastStep(true);
        title = name;
        steps = instructions;
     }

     public String getTitle(){
        return title;
     }
     public Step[] getSteps(){
        return steps;
     }
 }
