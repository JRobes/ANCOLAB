package stackingtable;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.AbstractOverrider;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class StackingTableLabelAccumulator extends AbstractOverrider {
	IDataProvider dataProvider;
	
	public StackingTableLabelAccumulator(IDataProvider dataProvider){
		this.dataProvider = dataProvider;
	}
	@Override
	public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
		@SuppressWarnings("rawtypes")
		AncolabStackingLayer mat = (AncolabStackingLayer) ((IRowDataProvider) dataProvider).getRowObject(rowPosition);
		System.out.println("dentro del acumulador ...................."+mat.getType());
		if((mat.getType().equals("Composite") && columnPosition == 4) || (mat.getType().equals("Honeycomb") && columnPosition == 3) ) {
			configLabels.addLabelOnTop(StackingTable.TEST);
		}
		else {
			System.out.println("noe es un composite es un oney");
		}
	}

}
