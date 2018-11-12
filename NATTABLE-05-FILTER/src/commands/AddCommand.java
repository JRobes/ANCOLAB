package commands;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import ca.odell.glazedlists.EventList;

public class AddCommand implements AddDeleteCommand {
	EventList<AncolabMaterial> eventList;
	
	
	public AddCommand(EventList list) {
		this.eventList = list;
		
	}
	
	@Override
	public void execute() {
	}

}
