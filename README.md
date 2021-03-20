# Programming Semantic Systems

In this project is needed to develop a basic semantic application enabled by a knowledge graph. In particular, to create a simple program that programmatically accesses a knowledge graph. 

## Run

```
cd `pwd`
java -jar movies_ss_jena.jar Main
```

If this command does not work then please use the instruction below.

This program is developed using `Eclipse` editor. 

In order to set up this project in `Eclipse`:
1. Load the project in `Eclipse`
2. From the [Jena](Jena) folder, create a new library with `jar` files inside this folder
3. Import the created library into the project

## How it works

Automatically an initial ontology from local file system => [movie.ttl](input/graph/movie.ttl) is loaded into the program. Features that are implemented are:

### 1. Activate reasoners

To assess the correctness of the ontology, couple of reasoners are implemented:
1. Transitive
2. RDFS
3. RDFS Simple
4. OWL
5. OWL Mini *and*
6. OWL Micro

To see the menu for activating implemented reasoners in the program, user must press **1** from the main menu and then for each of the implemented reasoners, in order to execute, user must press numbers from **1** to **6** - with **1** being the first implemented reasoner *(Transitive)* and **6** the last one *(OWL Micro)*.

### 2. Export Knowledge Graph

Ontology can be exported in the following formats:
1. JSONLD
2. NTRIPLES
3. TTL *and*
4. RDFS/XML

To see the menu for exporting knowledge graph in the program, user must press **2** from the main menu and then for each of the implemented exported formats, in order to execute, user must press numbers from **1** to **4** - with **1** being the first implemented format *(JSONLD)* and **4** the last one *(RDFS/XML)*.

The exported file can be found in [output](output) folder.

### 3. Add a new ontology class

User can create a new ontology class with the default namespace *http://semantics.id/ns/example/movie#* by pressing **3** from the main menu.

Newly created class can be set as a:
1. Super class of another class
2. Sub class of another class *or*
3. Stand alone *(leave it as it is)*

To set the created class as a super class of another class, user must press **1**, for sub class **2** and to leave it as it is **3**.

In order to avoid user input error, a list of class names to choose from is provided for both cases (super and sub class).

### 4. Add a new property

In order for the user to be able to add a new property to the ontology, user must press **4** from the main menu.

There are two options to create a new property:
1. Object Property *or*
2. Data Property

To create a new property as an *Object Property*, user must press **1**. In this case, to set the domain and range of the property to be created, a list of all available classes is provided for the user to choose from (to avoid user input error).

To create a new property as a *Data Property*, user must press **2**. In this case, to set the domain, a list of all available classes is provided for the user to choose from (to avoid user input error) but for the range of the property, user can select from these available data types:
1. Boolean
2. Float
3. Integer
4. Date
5. String

After selecting the data type (by pressing numbers from **1** for *Boolean* to **5** for *String*), user will need to type the name of the property and save it by pressing *enter*.

### 5. Add ontology instances

Using this feature, user is able to create ontology instances by:
1. Writing down the instance name
2. A list of all available ontology classes will be show to the user and the user will need to select a class in order to add this instance to
3. A list of all available *Object Properties* for the selected class will be show to the user to select from. User can select an *Object Property* or go to *Data Properties*
    - If an *Object Property* is selected, a list of all instances of that particular domain will be shown to the user to select from
4. If the user goes to *Data Properties*, a list of all available *Data Properties* for the selected class will be show to the user to select from. User can select a *Data Property* or go to the main menu
    - If a *Data Property* is selected, the user will have to input the value in the specified format *(data type)*.

### 6. List queries

Ten predefined queries are available in the [query](input/query) folder. These queries are then listed to the user by pressing **6** from the main menu.

User can select a query from the list by pressing numbers from **1** to **10**. After selection, the query gets executed and the results are shown below in the console.

### 7. Show RDF Data

This option will simply allow the user to print out the RDF data in *subject, predicate, object* format. User can execute this option by pressing **7** from the main menu.
