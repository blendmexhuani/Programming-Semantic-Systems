package id.semantics.helper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ValidityReport;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

public class Utility {

	public static final String NS_EXAMPLE = "http://semantics.id/ns/example#";
	public static final String NS_MOVIE = "http://semantics.id/ns/example/movie#";

	public static final String INPUT_FILE = "./input/graph/movie.ttl";

	public static final String QUERY = "./input/query/Q";
	public static final String OUTPUT_FILE = "./output/output";

	public final static String lineSeparator = "###################################################";

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void writeFile(String outputFile, Lang format, OntModel model)
			throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(outputFile);
		RDFDataMgr.write(fos, model, format);
		System.out.println("\nModel has been exported successfully.");
	}

	public static void validateSolution(Reasoner reasoner, OntModel model) {
		InfModel infmodel = ModelFactory.createInfModel(reasoner, model);
		ValidityReport validity = infmodel.validate();
		if (validity.isValid()) {
			System.out.println("\nOK");
		} else {
			System.out.println("\nConflicts");
			for (Iterator i = validity.getReports(); i.hasNext();) {
				System.out.println(" - " + i.next());
			}
		}
	}

	public static void runQuery(OntModel model, String queryFile, String type) {
		try {
			String queryString = readFile(queryFile, Charset.forName("UTF-8"));
			QueryExecution execution = QueryExecutionFactory.create(queryString, model);
			if (type.equals("SELECT")) {
				ResultSet resultSet = execution.execSelect();
				System.out.println("\n" + lineSeparator);
				ResultSetFormatter.out(resultSet);
				System.out.println(lineSeparator);
			} else if (type.equals("ASK")) {
				boolean resultSet = execution.execAsk();
				System.out.println("\n" + lineSeparator + "\n" + resultSet + "\n" + lineSeparator);
			} else if (type.equals("DESCRIBE")) {
				Model resultSet = execution.execDescribe();
				System.out.println("\n" + lineSeparator);
				RDFDataMgr.write(System.out, resultSet, Lang.TURTLE);
				System.out.println(lineSeparator);
			} else {
				Model resultSet = execution.execConstruct();
				System.out.println("\n" + lineSeparator);
				RDFDataMgr.write(System.out, resultSet, Lang.TURTLE);
				System.out.println(lineSeparator);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void addProperty(OntModel model, String typeOfProperty) {
		Scanner input;
		System.out.println("\n" + lineSeparator);
		TreeMap<Integer, String> dict_classes = new TreeMap<Integer, String>();
		Integer i = 1;
		System.out.println("List of active ontology classes:");
		for (Iterator<?> res = model.listClasses(); res.hasNext();) {
			String ontClass = res.next().toString();
			System.out.println((i) + ". " + ontClass);
			dict_classes.put(i++, ontClass);
		}
		System.out.println(lineSeparator + "\n");
		Resource domainResource = null;
		while (true) {
			try {
				System.out.print(
						"Write down the number of the class from the list above that you want to set as domain: ");
				input = new Scanner(System.in);
				int classNumber = input.nextInt();
				if (classNumber > 0 && classNumber <= dict_classes.size()) {
					domainResource = model.getResource(dict_classes.get(classNumber));
					break;
				} else {
					System.out.println("The number you entered is not in the list!");
				}
			} catch (Exception e) {
				System.out.println("Please enter a number!");
			}
		}
		if (typeOfProperty.equals("ObjectProperty")) {
			Resource rangeResource = null;
			while (true) {
				try {
					System.out.print(
							"Write down the number of the class from the list above that you want to set as range: ");
					input = new Scanner(System.in);
					int classNumber = input.nextInt();
					if (classNumber > 0 && classNumber <= dict_classes.size()) {
						rangeResource = model.getResource(dict_classes.get(classNumber));
						break;
					} else {
						System.out.println("The number you entered is not in the list!");
					}
				} catch (Exception e) {
					System.out.println("Please enter a number!");
				}
			}
			System.out.print("Property name: ");
			input = new Scanner(System.in);
			String propName = input.nextLine();
			Property prop = model.createResource(NS_MOVIE + propName, OWL.ObjectProperty).as(Property.class);
			prop.addProperty(RDFS.domain, domainResource);
			prop.addProperty(RDFS.range, rangeResource);
		} else {
			String stringOperations = "\nSelect the range of the property:\n" + "1. Boolean\n" + "2. Float\n"
					+ "3. Integer\n" + "4. Date\n" + "5. String\n\n" + "Press a number to continue: ";
			System.out.print(stringOperations);
			Resource propRange = XSD.xstring;
			try {
				input = new Scanner(System.in);
				int res = input.nextInt();
				switch (res) {
				case 1:
					propRange = XSD.xboolean;
					break;
				case 2:
					propRange = XSD.xfloat;
					break;
				case 3:
					propRange = XSD.xint;
					break;
				case 4:
					propRange = XSD.date;
				case 5:
					break;
				default:
					System.out.println("Default range is set as \"String\"");
					break;
				}
			} catch (Exception e) {
				System.out.println("Default range is set as \"String\"");
			}
			System.out.print("Property name: ");
			input = new Scanner(System.in);
			String propName = input.nextLine();
			Property prop = model.createResource(NS_MOVIE + propName, OWL.DatatypeProperty).as(Property.class);
			prop.addProperty(RDFS.domain, domainResource);
			prop.addProperty(RDFS.range, propRange);
		}
		System.out.println("\nProperty has been added successfully.");
	}

	public static void addInstance(OntModel model, Individual individual, OntProperty property, String typeOfProperty) {
		Scanner input;
		if (typeOfProperty.equals("O")) {
			TreeMap<Integer, String> dict_individuals = new TreeMap<Integer, String>();
			Integer i = 1;
			System.out.println("\n" + lineSeparator);
			System.out.println("List of active instances for the selected domain:");
			for (Iterator<?> res = model.listIndividuals(property.getRange()); res.hasNext();) {
				String indProp = res.next().toString();
				System.out.println((i) + ". " + indProp);
				dict_individuals.put(i++, indProp);
			}
			System.out.println(lineSeparator + "\n");
			if (dict_individuals.size() != 0) {
				Individual individualProperty = null;
				while (true) {
					try {
						System.out.print(
								"Write down the number of the instance from the list above that you want to select: ");
						input = new Scanner(System.in);
						int propNumber = input.nextInt();
						if (propNumber > 0 && propNumber <= dict_individuals.size()) {
							individualProperty = model.getIndividual(dict_individuals.get(propNumber));
							break;
						} else {
							System.out.println("The number you entered is not in the list!");
						}
					} catch (Exception e) {
						System.out.println("Please enter a number!");
					}
				}
				individual.addProperty(property, individualProperty);
				System.out.println("Instance has been added successfully.");
			} else {
				System.out.println("Instances for the selected domain do not exists!");
			}
		} else {
			String typeOf = null;
			try {
				typeOf = property.getRange().getLocalName();
			} catch (Exception e) {
				typeOf = "string";
			}
			while (true) {
				System.out.println("The data type of the selected property is: \"" + typeOf + "\"");
				String stringOperations = "Enter your value: " + 
										(typeOf.equals("date") ? "(format: 1990-01-15) " : 
											(typeOf.equals("boolean") ? "(format: true or false) " : ""));
				System.out.print(stringOperations);
				input = new Scanner(System.in);
				String value = input.nextLine();
				if (typeOf.equals("boolean")) {
					individual.addProperty(property, value.toLowerCase(), XSDDatatype.XSDboolean);
					break;
				} else if (typeOf.equals("int")) {
					try {
						Integer.parseInt(value);
						individual.addProperty(property, value, XSDDatatype.XSDint);
						break;
					} catch (Exception e) {
						System.out.println("Incorrect input!");
					}
				} else if (typeOf.equals("float")) {
					try {
						Float.parseFloat(value);
						individual.addProperty(property, value, XSDDatatype.XSDfloat);
						break;
					} catch (Exception e) {
						System.out.println("Incorrect input!");
					}
				} else if (typeOf.equals("date")) {
					individual.addProperty(property, value, XSDDatatype.XSDdate);
					break;
				} else {
					individual.addProperty(property, value);
					break;
				}
			}
		}
	}

}
