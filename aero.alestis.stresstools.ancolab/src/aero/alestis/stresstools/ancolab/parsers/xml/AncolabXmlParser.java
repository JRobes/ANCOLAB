package aero.alestis.stresstools.ancolab.parsers.xml;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import aero.alestis.stresstools.ancolab.model.Key;
import aero.alestis.stresstools.ancolab.model.Material;
import aero.alestis.stresstools.ancolab.model.Materials;
import aero.alestis.stresstools.ancolab.model.Properties;
import aero.alestis.stresstools.ancolab.model.Property;

public class AncolabXmlParser {
	// private boolean inputFile;
	private JAXBContext context;
	private Unmarshaller um;
	//private AncolabMaterial parsedAncolabStackingMaterial;
	private List<AncolabMaterial> parsedListOfAncolabStackingMaterials;

	public AncolabXmlParser() {
		try {
			context = JAXBContext.newInstance(Materials.class);
			um = context.createUnmarshaller();
			//parsedListOfAncolabStackingMaterials = new ArrayList<AncolabMaterial>();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public List<AncolabMaterial> parseFolder(String inputFile) {
		List<AncolabMaterial> listAncolabStackingMaterial = new ArrayList<AncolabMaterial>();
		File folder = new File(inputFile);
		if(!folder.isDirectory()) {
			System.err.println("File path does not correspond with a directory");
			return null;
		}
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				// System.out.println("File " + listOfFiles[i].getAbsolutePath());
				
				listAncolabStackingMaterial.addAll(parseFile(listOfFiles[i].getAbsolutePath()));
				
			} else if (listOfFiles[i].isDirectory()) {
				// System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
		this.parsedListOfAncolabStackingMaterials = listAncolabStackingMaterial;
		return listAncolabStackingMaterial;
	}

	public List<AncolabMaterial> parseFile(String inputFile) {
		List<AncolabMaterial> listAnc = new ArrayList<AncolabMaterial>();
		Materials materialProperties = null;
		try {
			materialProperties = (Materials) um.unmarshal(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			//e.printStackTrace();
			System.err.println("The file " + inputFile + " is not parseable to AncolabStackingMaterial ");
			return listAnc; 
		}
		// System.out.println(materialProperties.getListOfMaterials().size());
		for (Material mat : materialProperties.getListOfMaterials()) {
			String[] ancolabProps = new String[8];
			AncolabMaterial anc;
			if (mat.getType().equals("Composite")) {
				// String[] ancolabProps = new String[6];
				ancolabProps[0] = mat.getMaterialName();
				ancolabProps[5] = getTheThickness(mat.getMaterialCharacteristics().getListOfkeys(), "Thickness").get()
								  .getValue();
				ancolabProps[1] = getTheValue2(
								  getTheValue(getTheValue(mat.getMaterialProperties().getListOfMaterialProperties(), "Composite")
								  .get().getListOfProperties(), "Elastic moduli & poisson coeffs").get().getListOfProperty(),
								  "E1").get().getValue();
				ancolabProps[2] = getTheValue2(
						getTheValue(getTheValue(mat.getMaterialProperties().getListOfMaterialProperties(), "Composite")
								.get().getListOfProperties(), "Elastic moduli & poisson coeffs").get()
										.getListOfProperty(),
						"E2").get().getValue();
				ancolabProps[3] = getTheValue2(
						getTheValue(getTheValue(mat.getMaterialProperties().getListOfMaterialProperties(), "Composite")
								.get().getListOfProperties(), "Elastic moduli & poisson coeffs").get()
										.getListOfProperty(),
						"G12").get().getValue();
				ancolabProps[4] = getTheValue2(
						getTheValue(getTheValue(mat.getMaterialProperties().getListOfMaterialProperties(), "Composite")
								.get().getListOfProperties(), "Elastic moduli & poisson coeffs").get()
										.getListOfProperty(),
						"nu12").get().getValue();
				ancolabProps[6] = mat.getType();
				ancolabProps[7] = mat.getLibrary();
				anc = new AncolabMaterial(ancolabProps[0], //name
						                  ancolabProps[7], //library
						                  ancolabProps[6], //type
						                  ancolabProps[5], //thickness
						                  ancolabProps[1], //E1
						                  ancolabProps[2], //E2
						                  ancolabProps[3], //G12
						                  ancolabProps[4]);//nu12
				listAnc.add(anc);
				
				System.out.print(ancolabProps[0] + "\t\t");
				System.out.print(ancolabProps[1] + "\t");
				System.out.print(ancolabProps[2] + "\t");
				System.out.print(ancolabProps[3] + "\t");
				System.out.print(ancolabProps[4] + "\t");
				System.out.print(ancolabProps[5] + "\t");
				System.out.print(ancolabProps[6] + "\t");
				System.out.print(ancolabProps[7] + "\n");
				
			} else if (mat.getType().equals("Honeycomb")) {
				ancolabProps[0] = mat.getMaterialName();
				ancolabProps[6] = mat.getType();
				ancolabProps[7] = mat.getLibrary();
				anc = new AncolabMaterial(	ancolabProps[0], //name
		                  					ancolabProps[7], //library
		                  					ancolabProps[6], //type
		                  							  "0.0", //thickness
		                  							  "0.1", //E1
		                  							  "0.1", //E2
		                  							  "0.1", //G12
		                  							  "0.1");//nu12

				System.out.print(anc.getName() + "\t\t");
				System.out.print(anc.getLibrary() + "\t");
				System.out.print(anc.getE1() + "\t");
				System.out.print(anc.getE2() + "\t");
				System.out.print(anc.getG12() + "\t");
				System.out.print(anc.getNu12() + "\t");
				System.out.print(anc.getThickness() + "\t");
				System.out.print(anc.getType() + "\n");

				listAnc.add(anc);
			}
		}
		this.parsedListOfAncolabStackingMaterials = listAnc;
		return listAnc;
	}
	
	public void serialize(String outputPath) {
        try {
        	FileOutputStream os =new FileOutputStream("C:\\Users\\javier.robes\\Desktop\\TAREAS\\ANCOLAB-MATERIALS\\serial.ser");
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(parsedListOfAncolabStackingMaterials);
            oos.flush();
        }catch(EOFException eof){
    
        } 
        catch (Exception e) {
            e.printStackTrace();
          
        }
	}

	private static Optional<Key> getTheThickness(final List<Key> list, final String name) {
		return list.stream().filter(o -> o.getName().equals(name)).findFirst();
	}

	private static Optional<Property> getTheValue2(final List<Property> list, final String name) {
		return list.stream().filter(o -> o.getName().equals(name)).findFirst();
	}

	private static Optional<Properties> getTheValue(final List<Properties> list, final String name) {
		return list.stream().filter(o -> o.getName().equals(name)).findFirst();
	}

}
