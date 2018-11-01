package aero.alestis.stresstools.ancolab.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "materialCharacteristics")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MaterialCharacteristics {
	private List<Key> listOfKeys = null;
	
	MaterialCharacteristics(){
		listOfKeys = new ArrayList<Key>();
	}

	public List<Key> getListOfkeys() {
		return listOfKeys;
	}
	@XmlElement(name = "key")
	public void setListOfkeys(List<Key> listOfkeys) {
		this.listOfKeys = listOfkeys;
	}
	
}
