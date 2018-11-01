package aero.alestis.stresstools.ancolab.business;

import java.util.List;

import aero.alestis.stresstools.ancolab.db.FacadeDbAncolabMaterials;
import aero.alestis.stresstools.ancolab.db.FacadeDbAncolabMaterials.LoadSaveStrategy;
import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public enum FacadeBusinessAncolabMaterials {
    INSTANCE;

	
	public List<AncolabMaterial> getAllMaterials(LoadSaveStrategy strategy, String filePath){
		return FacadeDbAncolabMaterials.INSTANCE.getAllMaterials(strategy, filePath);
				
		
	}
	
	public void saveAllMaterials(LoadSaveStrategy strategy, String filePath, List<AncolabMaterial> materialsToSave) {
		FacadeDbAncolabMaterials.INSTANCE.saveAllMaterials(strategy, filePath, materialsToSave);
	}
}
