package nattable05filter.adddeleterows;

import org.eclipse.nebula.widgets.nattable.command.AbstractRowCommand;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public class AddRowCommand extends AbstractRowCommand {
	private AncolabMaterial item;
	public AddRowCommand(ILayer layer, AncolabMaterial item) {
		super(layer, 0);
		//this.setItem(item);
		this.item = item;
	}
	/*
	public AddRowCommand(ILayer layer, int rowPosition) {
		super(layer, rowPosition);
	}
	*/
	protected AddRowCommand(AddRowCommand command) {
		super(command);
	}
	@Override
	public ILayerCommand cloneCommand() {
		return new AddRowCommand(this);
	}
	public AncolabMaterial getItem() {
		return this.item;
	}
	private void setItem(AncolabMaterial item) {
		this.item = item;
	}

}
