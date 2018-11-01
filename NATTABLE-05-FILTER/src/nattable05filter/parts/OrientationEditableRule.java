package nattable05filter.parts;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;

import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;

import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;



public class OrientationEditableRule implements IEditableRule{
	@SuppressWarnings("unused")
	private int columnNumber;
	private String propertyName;
	private ISelectionProvider selectionProvider;
	//private  RowSelectionProvider row;
	public OrientationEditableRule(ISelectionProvider selectionProvider, String propertyName) {
		//this.columnNumber = columnNumber;
		this.propertyName = propertyName;
		this.selectionProvider= selectionProvider;
		//System.out.println(data.getRowCount());
	}
	
	@Override
	public boolean isEditable(ILayerCell cell, IConfigRegistry configRegistry) {
		
		return isEditable(cell.getColumnIndex(), cell.getRowIndex());
	}
	

	@Override
	public boolean isEditable(int columnIndex, int rowIndex) {
		
		IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
		AncolabStackingLayer selected = (AncolabStackingLayer)selection.getFirstElement();
		
		if(columnIndex == 1 && !selected.getType().equals(this.propertyName)) {
			System.out.println("La celda es editable");
			return true;// && value.equals("Honeycomb"))
		}
		System.out.println("Esta celda no es editable");

		return false;
	}
	
}


