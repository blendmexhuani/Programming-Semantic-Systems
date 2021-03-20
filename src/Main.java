import id.semantics.helper.Tutorial;
import id.semantics.implementation.ApacheJena;

import java.io.IOException;
import java.util.Scanner;

import static id.semantics.helper.Utility.*;

public class Main {

    public static void main(String[] args) throws IOException {
    	// Automatically loading an initial ontology model from local file system
    	Tutorial tutorial = new ApacheJena();
        tutorial.loadRdfFile(INPUT_FILE);
    	int i = -1;
    	while(i != 0){
    		String lineDivision = "#############################################\n";
    		String stringOperations = "\n" + lineDivision + lineDivision
    								+ "####\t 1. Activate reasoners           ####\n"
    								+ "####\t 2. Export Knowledge Graph       ####\n"
    								+ "####\t 3. Add a new ontology class     ####\n"
    								+ "####\t 4. Add a new property           ####\n"
    								+ "####\t 5. Add ontology instances       ####\n"
    								+ "####\t 6. List queries                 ####\n"
    								+ "####\t 7. Show RDF Data                ####\n"
    								+ "####\t 0. Exit the application         ####\n"
    								+ lineDivision + lineDivision + "\n";
    		System.out.print(stringOperations);
    		Scanner input= new Scanner(System.in);
	        System.out.print("Press a number to continue: "); 
	        try{
	        	i = input.nextInt();
	        } catch (Exception e) {
				System.out.println("Please enter a number from 0 to 6!");
				continue;
			}	        
	        switch(i){
	        	case 1: tutorial.Activate_Resoners();
	        		break;
	        	case 2: tutorial.Export_KG();
	        		break;
	        	case 3: tutorial.Add_Class();
	        		break;
	        	case 4: tutorial.Add_Property();
	        		break;
	        	case 5: tutorial.Add_Instace();
	        		break;
	        	case 6: tutorial.List_Queries();
	        		break;
	        	case 7: tutorial.iteratingRdfData();
        			break;
	        	case 0:
	        	default: continue;
	        }
    	}
    }
}
