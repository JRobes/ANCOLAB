package aero.alestis.stresstools.ancolab.db;

import java.util.List;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public abstract class MaterialsServiceStrategy {
	protected String filePath;
	abstract List<AncolabMaterial> getMaterials() ;
	abstract List<AncolabMaterial> saveMaterialList(String thePath, List<AncolabMaterial> materialsToSave);
}
