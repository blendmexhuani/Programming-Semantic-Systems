package id.semantics.helper;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Tutorial {

    public void createRepository();

    public void loadRdfFile(String inputFile) throws FileNotFoundException, IOException;

    public void iteratingRdfData();
    
    public void Activate_Resoners();

    public void Export_KG() throws FileNotFoundException, IOException;
    
    public void Add_Class();
    
    public void Add_Property();
    
    public void Add_Instace();
    
    public void List_Queries();
    
}
