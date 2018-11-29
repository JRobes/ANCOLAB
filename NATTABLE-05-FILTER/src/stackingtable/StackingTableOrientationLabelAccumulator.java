package stackingtable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.AbstractOverrider;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class StackingTableOrientationLabelAccumulator extends AbstractOverrider {
	IDataProvider dataProvider;
	
	public StackingTableOrientationLabelAccumulator(IDataProvider dataProvider){
		this.dataProvider = dataProvider;
	}
	@Override
	public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
		AncolabStackingLayer mat = (AncolabStackingLayer) ((IRowDataProvider<?>) dataProvider).getRowObject(rowPosition);
		OrientationDataValidator orientationDataValidator = new OrientationDataValidator((IRowDataProvider<?>) dataProvider);
		ThicknessDataValidator thicknessDataValidator = new ThicknessDataValidator((IRowDataProvider<?>) dataProvider);
		if(mat.getType().equals("Composite")) {
			if(!orientationDataValidator.validate(columnPosition, rowPosition, mat) && columnPosition == 3) {
				configLabels.addLabelOnTop(StackingTable.INVALID_ANGLE_LABEL);
			}
			else {
			configLabels.removeLabel(StackingTable.INVALID_ANGLE_LABEL);
			}
		}
		else {
			if(!thicknessDataValidator.validate(columnPosition, rowPosition, mat) && columnPosition == 4) {
				configLabels.addLabelOnTop(StackingTable.INVALID_THICKNESS_LABEL);
			}
			else {
				configLabels.removeLabel(StackingTable.INVALID_THICKNESS_LABEL);
			}

			
		}

	}

}
