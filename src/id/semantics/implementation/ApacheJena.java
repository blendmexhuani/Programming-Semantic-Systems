package id.semantics.implementation;

import id.semantics.helper.Tutorial;
import id.semantics.helper.Utility;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDFS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import java.util.Iterator;
import java.util.Map;

public class ApacheJena implements Tutorial {

	private OntModel ontModel;

	public ApacheJena() {
		createRepository();
	}

	@Override
	public void createRepository() {
		ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
	}

	@Override
	public void loadRdfFile(String inputFile) throws FileNotFoundException, IOException {
		RDFDataMgr.read(ontModel, inputFile);
	}

	@Override
	public void iteratingRdfData() {
		ontModel.listStatements().forEachRemaining(statement -> {
			System.out.println(statement);
		});
	}

	@Override
	public void Activate_Resoners() {
		String stringOperations = "\nWhich of the following reasoners do you want to activate:\n" + "1. Transitive\n"
				+ "2. RDFS\n" + "3. RDFS Simple\n" + "4. OWL\n" + "5. OWL Mini\n" + "6. OWL Micro\n\n";
		System.out.print(stringOperations);
		boolean skipWhile = false;
		while (true) {
			System.out.print("Press a number to continue: ");
			int res = 0;
			try {
				Scanner input = new Scanner(System.in);
				res = input.nextInt();
				switch (res) {
				case 1:
					Utility.validateSolution(ReasonerRegistry.getTransitiveReasoner(), ontModel);
					skipWhile = true;
					break;
				case 2:
					Utility.validateSolution(ReasonerRegistry.getRDFSReasoner(), ontModel);
					skipWhile = true;
					break;
				case 3:
					Utility.validateSolution(ReasonerRegistry.getRDFSSimpleReasoner(), ontModel);
					skipWhile = true;
					break;
				case 4:
					Utility.validateSolution(ReasonerRegistry.getOWLReasoner(), ontModel);
					skipWhile = true;
					break;
				case 5:
					Utility.validateSolution(ReasonerRegistry.getOWLMiniReasoner(), ontModel);
					skipWhile = true;
					break;
				case 6:
					Utility.validateSolution(ReasonerRegistry.getOWLMicroReasoner(), ontModel);
					skipWhile = true;
					break;
				default:
					System.out.println("No reasoner has been selected!");
					break;
				}
				if (skipWhile)
					break;
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
	}

	@Override
	public void Export_KG() throws FileNotFoundException, IOException {
		String stringOperations = "\nSelect the format in which you want to export:\n" + "1. JSONLD\n" + "2. NTRIPLES\n"
				+ "3. TTL\n" + "4. RDFS/XML\n\n";
		System.out.print(stringOperations);
		boolean skipWhile = false;
		while (true) {
			System.out.print("Press a number to continue: ");
			int res = 0;
			try {
				Scanner input = new Scanner(System.in);
				res = input.nextInt();
				switch (res) {
				case 1:
					Utility.writeFile(Utility.OUTPUT_FILE + ".jsonld", Lang.JSONLD, ontModel);
					skipWhile = true;
					break;
				case 2:
					Utility.writeFile(Utility.OUTPUT_FILE + ".nt", Lang.NTRIPLES, ontModel);
					skipWhile = true;
					break;
				case 3:
					Utility.writeFile(Utility.OUTPUT_FILE + ".ttl", Lang.TTL, ontModel);
					skipWhile = true;
					break;
				case 4:
					Utility.writeFile(Utility.OUTPUT_FILE + ".owl", Lang.RDFXML, ontModel);
					skipWhile = true;
					break;
				default:
					System.out.println("No format has been selected!");
					break;
				}
				if (skipWhile)
					break;
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
	}

	@Override
	public void Add_Class() {
		String stringOperations = "\nEnter the new class name: ";
		System.out.print(stringOperations);
		Scanner input = new Scanner(System.in);
		String className = input.nextLine();
		OntClass createdClass = ontModel.createClass(Utility.NS_MOVIE + className);
		stringOperations = "\nShould this class be:\n" + "1. Super class of another class\n"
				+ "2. Sub class of another class\n" + "3. Stand alone\n\n";
		System.out.print(stringOperations);
		boolean skipWhile = false;
		while (true) {
			try {
				System.out.print("Press a number to continue: ");
				input = new Scanner(System.in);
				int res = input.nextInt();
				if (res == 1 || res == 2) {
					System.out.println("\nChoose a class from the list below:");
					TreeMap<Integer, String> dict_classes = new TreeMap<Integer, String>();
					Integer i = 1;
					for (Iterator<?> result = ontModel.listClasses(); result.hasNext();) {
						String ontClass = result.next().toString();
						if (ontClass.equals(createdClass.getURI()))
							continue;
						System.out.println(i + ". " + ontClass);
						dict_classes.put(i++, ontClass);
					}
					System.out.println();
					while (true) {
						try {
							System.out.print("Press a number to continue: ");
							input = new Scanner(System.in);
							int classNumber = input.nextInt();
							if (classNumber > 0 && classNumber <= dict_classes.size()) {
								Resource resource = ontModel.getResource(dict_classes.get(classNumber));
								if (res == 1) {
									createdClass.addSubClass(resource);
								} else {
									createdClass.addSuperClass(resource);
								}
								skipWhile = true;
								break;
							} else {
								System.out.println("The number you entered is not in the list!");
							}
						} catch (Exception e) {
							System.out.println("Please enter a number!");
						}
					}
					if (skipWhile)
						break;
				} else if (res == 3)
					break;
				else {
					System.out.println("The number you entered is not in the list!");
				}
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}

		System.out.println("\nClass has been added successfully.");
	}

	@Override
	public void Add_Property() {
		String stringOperations = "\nWhich type of property do you want to add:\n" + "1. Object property\n"
				+ "2. Data property\n\n";
		System.out.print(stringOperations);
		while (true) {
			try {
				System.out.print("Press a number to continue: ");
				Scanner input = new Scanner(System.in);
				int res = input.nextInt();
				if (res == 1) {
					Utility.addProperty(ontModel, "ObjectProperty");
					break;
				} else if (res == 2) {
					Utility.addProperty(ontModel, "DataProperty");
					break;
				} else
					System.out.println("The number you entered is not in the list!");
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
	}

	@Override
	public void Add_Instace() {
		String stringOperations = "\nEnter the instance name: ";
		System.out.print(stringOperations);
		Scanner input = new Scanner(System.in);
		String instanceName = input.nextLine();
		TreeMap<Integer, String> dict_classes = new TreeMap<Integer, String>();
		Integer i = 1;
		System.out.println("\n" + Utility.lineSeparator);
		System.out.println("List of active ontology classes:");
		for (Iterator<?> res = ontModel.listClasses(); res.hasNext();) {
			String ontClass = res.next().toString();
			System.out.println((i) + ". " + ontClass);
			dict_classes.put(i++, ontClass);
		}
		System.out.println(Utility.lineSeparator + "\n");
		Resource resource = null;
		while (true) {
			try {
				System.out.print(
						"Write down the number of the CLASS from the list above that you want to add instance to: ");
				input = new Scanner(System.in);
				int classNumber = input.nextInt();
				if (classNumber > 0 && classNumber <= dict_classes.size()) {
					resource = ontModel.getResource(dict_classes.get(classNumber));
					break;
				} else {
					System.out.println("The number you entered is not in the list!");
				}
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
		Individual newIndividual = ontModel.createIndividual(Utility.NS_EXAMPLE + instanceName, resource);
		newIndividual.addProperty(RDFS.label, instanceName);

		// OBJECT PROPERTIES
		TreeMap<Integer, String> dict_objProperties = new TreeMap<Integer, String>();
		i = 1;
		System.out.println("\n" + Utility.lineSeparator);
		System.out.println("List of active ontology object properties for " + resource.getLocalName() + ":");
		for (Iterator<?> res = ontModel.listObjectProperties(); res.hasNext();) {
			OntProperty objProp = (OntProperty) res.next();
			if (ontModel.getOntClass(resource.getURI()).hasSuperClass(objProp.getDomain())) {
				System.out.println((i) + ". " + objProp.toString());
				dict_objProperties.put(i++, objProp.toString());
			}
		}
		System.out.println(i + ". Go to DATA PROPERTIES");
		System.out.println("0. To exit");
		System.out.println(Utility.lineSeparator + "\n");
		Property objProperty = null; boolean skipDataProperties = false;
		while (true) {
			try {
				System.out.print("Write down the number of the OBJECT PROPERTY from the list above that you want to add this instance to: ");
				input = new Scanner(System.in);
				int propNumber = input.nextInt();
				if (propNumber > 0 && propNumber <= dict_objProperties.size()) {
					objProperty = ontModel.getProperty(dict_objProperties.get(propNumber));
					Utility.addInstance(ontModel, newIndividual, (OntProperty) objProperty, "O");
					System.out.println("\n" + Utility.lineSeparator);
					Iterator k = dict_objProperties.entrySet().iterator();
					while (k.hasNext()) {
						Map.Entry value = (Map.Entry) k.next();
						System.out.println(value.getKey() + ". " + value.getValue());
					}
					System.out.println(i + ". Go to DATA PROPERTIES");
					System.out.println("0. To exit");
					System.out.println(Utility.lineSeparator + "\n");
				} else if (propNumber == dict_objProperties.size() + 1) { 
					break;
				} else {
					if (propNumber == 0) {
						skipDataProperties = true;
						break;
					}
					System.out.println("The number you entered is not in the list!");
				}
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}

		if (skipDataProperties) { return; }
		// DATA PROPERTIES
		TreeMap<Integer, String> dict_dataProperties = new TreeMap<Integer, String>();
		i = 1;
		System.out.println("\n" + Utility.lineSeparator);
		System.out.println("List of active ontology data properties for " + resource.getLocalName() + ":");
		for (Iterator<?> res = ontModel.listDatatypeProperties(); res.hasNext();) {
			OntProperty dataProp = (OntProperty) res.next();
			if (ontModel.getOntClass(resource.getURI()).hasSuperClass(dataProp.getDomain())) {
				System.out.println((i) + ". " + dataProp.toString());
				dict_dataProperties.put(i++, dataProp.toString());
			}
		}
		System.out.println("0. To exit");
		System.out.println(Utility.lineSeparator + "\n");
		Property dataProperty = null;
		while (true) {
			try {
				System.out.print("Write down the number of the DATA PROPERTY from the list above that you want to add this instance to: ");
				input = new Scanner(System.in);
				int propNumber = input.nextInt();
				if (propNumber > 0 && propNumber <= dict_dataProperties.size()) {
					dataProperty = ontModel.getProperty(dict_dataProperties.get(propNumber));
					Utility.addInstance(ontModel, newIndividual, (OntProperty) dataProperty, "D");
					System.out.println("\n" + Utility.lineSeparator);
					Iterator k = dict_dataProperties.entrySet().iterator();
					while (k.hasNext()) {
						Map.Entry value = (Map.Entry) k.next();
						System.out.println(value.getKey() + ". " + value.getValue());
					}
					System.out.println("0. To exit");
					System.out.println(Utility.lineSeparator + "\n");
				} else {
					if (propNumber == 0)
						break;
					System.out.println("The number you entered is not in the list!");
				}
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
	}

	@Override
	public void List_Queries() {
		String stringOperations = "\nList of predefined queries:\n"
				+ " 1. SELECT - Return all movies with their title\n"
				+ " 2. ASK - Is there an actress named 'Emma Watson'?\n"
				+ " 3. DESCRIBE - Give me all information about movies where Emma Watson is playing\n"
				+ " 4. CONSTRUCT - Return all movies with their title\n"
				+ " 5. CONSTRUCT - Which movie writer has also directed the movie\n"
				+ " 6. FILTER - Select unique name of actors who play in movies released after 2016\n"
				+ " 7. FILTER - Show ordered unique names of the directors who directed in movies released between 2016 and 2018, and from 'Walt Disney Pictures' film studio\n"
				+ " 8. GROUP BY - Count the number of actors involved in each movie!\n"
				+ " 9. GROUP BY & HAVING - Select movies which have more than two genres\n"
				+ "10. UNION - List all actors and crews together with the title of movies that they’re involved ordered by their name\n";
		System.out.println(stringOperations);
		boolean skipWhile = false;
		while (true) {
			try {
				System.out.print("Select a number to execute a query or 0 to exit: ");
				Scanner input = new Scanner(System.in);
				int result = input.nextInt();
				switch (result) {
				case 0:
					skipWhile = true;
					break;
				case 1:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					Utility.runQuery(ontModel, Utility.QUERY + result + ".sparql", "SELECT");
					skipWhile = true;
					break;
				case 2:
					Utility.runQuery(ontModel, Utility.QUERY + result + ".sparql", "ASK");
					skipWhile = true;
					break;
				case 3:
					Utility.runQuery(ontModel, Utility.QUERY + result + ".sparql", "DESCRIBE");
					skipWhile = true;
					break;
				case 4:
				case 5:
					Utility.runQuery(ontModel, Utility.QUERY + result + ".sparql", "CONSTRUCT");
					skipWhile = true;
					break;
				default:
					System.out.println("Please select a number from 1 to 10!");
					break;
				}
				if (skipWhile)
					break;
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
	}
}
