package stackingtable;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class StackingTableThicknessLabelAccumulator extends org.eclipse.nebula.widgets.nattable.layer.cell.AbstractOverrider {
	IDataProvider dataProvider;
	
	public StackingTableThicknessLabelAccumulator(IDataProvider dataProvider){
		this.dataProvider = dataProvider;
	}
	@Override
	public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
		AncolabStackingLayer mat = (AncolabStackingLayer) ((IRowDataProvider) dataProvider).getRowObject(rowPosition);
		ThicknessDataValidator thicknessDataValidator = new ThicknessDataValidator((IRowDataProvider<?>) dataProvider);
		if(!thicknessDataValidator.validate(columnPosition, rowPosition, mat) && columnPosition == 4) {
			System.out.println("%%%%%%%%%%%%%%%%%%%%% INVALIDO %%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			configLabels.addLabelOnTop(StackingTable.INVALID_THICKNESS_LABEL);
		}
		else {
			System.out.println("%%%%%%%%%         %%%%%%%% VALIDO %%%%%%%%%%           %%%%%%%%%%%%");

		configLabels.removeLabel(StackingTable.INVALID_THICKNESS_LABEL);
		}
			
	}

}
