package aero.alestis.stresstools.ancolab.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public class SerializableFileStrategy extends MaterialsServiceStrategy {

	public SerializableFileStrategy(String file) {
		super.filePath = file;
		//"C:\\Users\\javier.robes\\Desktop\\TAREAS\\ANCOLAB-MATERIALS\\serial.ser"
	}
	
	@SuppressWarnings("unchecked")
	@Override
	List<AncolabMaterial> getMaterials() {
		List<AncolabMaterial>listOfMaterials  = new ArrayList<AncolabMaterial>();
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(filePath));
			listOfMaterials= (List<AncolabMaterial>) in.readObject();
			in.close();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println("Importados "+listOfMaterials.size()+" materiales...");
		return listOfMaterials;		
	}

	@Override
	List<AncolabMaterial> saveMaterialList(String path, List<AncolabMaterial> materialsToSave) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(materialsToSave);
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		//System.out.println(listOfMaterials.size());
		//return listOfMaterials;		
		return null;
	}

}
