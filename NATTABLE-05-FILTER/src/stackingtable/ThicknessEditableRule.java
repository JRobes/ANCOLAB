package stackingtable;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;


public class ThicknessEditableRule implements IEditableRule{
	private String propertyName;
	private IDataProvider dataProvider;
	private int columnNumber;
	
	public ThicknessEditableRule(IDataProvider dataProvider, String propertyName, int columnNumber) {
		this.propertyName  = propertyName;
		this.dataProvider  = dataProvider;
		this.columnNumber  = columnNumber;
	}

	@Override
	public boolean isEditable(ILayerCell cell, IConfigRegistry configRegistry) {
		//ILayer.getCellByPosition
		return isEditable(cell.getColumnIndex(), cell.getRowIndex());
	}

	@Override
	public boolean isEditable(int columnIndex, int rowIndex) {
		@SuppressWarnings("rawtypes")
		AncolabStackingLayer selection =  (AncolabStackingLayer) ((IRowDataProvider)dataProvider).getRowObject(rowIndex);
		if(columnIndex == columnNumber && selection.getType().equals(this.propertyName)) {
			System.out.println("La celda es editable");
			return true;// && value.equals("Honeycomb"))
		}
		System.out.println("Esta celda no es editable");

		return false;
	}
}


