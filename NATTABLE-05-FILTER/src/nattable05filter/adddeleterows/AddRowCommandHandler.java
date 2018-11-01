package nattable05filter.adddeleterows;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.command.ILayerCommandHandler;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.event.RowInsertEvent;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;


public class AddRowCommandHandler<AncolabMaterial> implements ILayerCommandHandler<AddRowCommand> {
	private List<AncolabMaterial> bodyData;

    public AddRowCommandHandler(List<AncolabMaterial> bodyData) {
        this.bodyData = bodyData;
    }
	
	@Override
	public Class<AddRowCommand> getCommandClass() {
		return AddRowCommand.class;
	}

	@Override
	public boolean doCommand(ILayer targetLayer,  AddRowCommand command) {
		
		AncolabMaterial mat = (AncolabMaterial) command.getItem();
		System.out.println("ENTRA DENTRO DE doCommand--------------");
		System.out.println("Target layer  " +targetLayer.toString());
		System.out.println("Aquí se sabe si el command lleva el material+++++++++++++++++++++--------------");
		//System.out.println(mat.getName());
		System.out.println("Aquí se sabe si el command lleva el material+++++++++++++++++++++--------------");
		
		//if (command.convertToTargetLayer(targetLayer)) {
			this.bodyData.add(0, (AncolabMaterial)command.getItem() );
			System.out.println("FFFFFFFFFFFFFFFFf "+this.bodyData.size());
			
            targetLayer.fireLayerEvent(new RowInsertEvent(targetLayer, command.getRowPosition()));
            //System.out.println(((DataLayer) targetLayer).getRowCount());
			return true;
		//}
		//return false;
	}

}
