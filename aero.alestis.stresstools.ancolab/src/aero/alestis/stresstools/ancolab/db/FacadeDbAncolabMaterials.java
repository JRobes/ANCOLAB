package aero.alestis.stresstools.ancolab.db;

import java.util.List;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public enum FacadeDbAncolabMaterials {
	INSTANCE;
	public enum LoadSaveStrategy {SERIALIZABLE, DATABASE};
	private String fileName;
	
	public List<AncolabMaterial> getAllMaterials(LoadSaveStrategy loadSaveStrategy, String fileName) {
		if (loadSaveStrategy == LoadSaveStrategy.SERIALIZABLE) {
			
			MaterialsServiceStrategy strategy = new SerializableFileStrategy(fileName);
			
			return strategy.getMaterials();

		}
		else {
			System.err.println("no implementado esta forma de obtener los datos...");
			return null;
		}
	}
	public void saveAllMaterials(LoadSaveStrategy loadSaveStrategy, String fileName, List<AncolabMaterial> listToSave) {
		if (loadSaveStrategy == LoadSaveStrategy.SERIALIZABLE) {
			System.out.println("Entro en facade Db save All Materials..");
			MaterialsServiceStrategy strategy = new SerializableFileStrategy(fileName);
			strategy.saveMaterialList(fileName, listToSave);
			

		}
		else {
			System.err.println("no implementado esta forma de guardar, salvar los datos...");
		}
	}
	
}
