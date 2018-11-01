package nattable05filter.parts;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Point;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;

public class DragAndDropSupport implements DragSourceListener, DropTargetListener{
    private final NatTable natTable;
    private final SelectionLayer selectionLayer;
    @SuppressWarnings("rawtypes")
	private final List data;
    
    private AncolabMaterial materialDragged; 
    private static final String DATA_SEPARATOR = "|";


    @SuppressWarnings("rawtypes")
	public DragAndDropSupport(NatTable natTable, SelectionLayer selectionLayer, List data) {
        this.natTable = natTable;
        this.selectionLayer = selectionLayer;
        this.data = data;
    }


	@SuppressWarnings("rawtypes")
	@Override
	public void dragStart(DragSourceEvent event) {
		//event.detail =DND.DROP_COPY;

		System.out.println("Selection Layer numero columnas "+ this.selectionLayer.getColumnCount());
		System.out.println("Selection Layer numero filas"+ this.selectionLayer.getRowCount());
		
		if (this.selectionLayer.getSelectedRowCount() == 0) {
            event.doit = false;
            System.out.println("Selection Layer Row Count == 0");

        } else if (!this.natTable.getRegionLabelsByXY(event.x, event.y).hasLabel(GridRegion.BODY)) {
            event.doit = false;
            System.out.println("RegionLabelsByXY fuera de zona....");

        }
        System.out.println("... dragStart ...");
        //ESTO ES PARA ELIMINAR
        List selection = ((RowSelectionModel) this.selectionLayer.getSelectionModel()).getSelectedRowObjects();
        this.materialDragged = (AncolabMaterial) selection.get(0);
        System.out.println("Name:\t" + this.materialDragged.getName());
        
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void dragSetData(DragSourceEvent event) {
        // we know that we use the RowSelectionModel with single selection
        List selection = ((RowSelectionModel) this.selectionLayer.getSelectionModel()).getSelectedRowObjects();

        if (!selection.isEmpty()) {
            this.materialDragged = (AncolabMaterial) selection.get(0);
            StringBuilder builder = new StringBuilder();
            builder.append(this.materialDragged.getName())
                    .append(DATA_SEPARATOR)
                    .append(this.materialDragged.getLibrary())
                    .append(DATA_SEPARATOR)
                    .append(this.materialDragged.getType())
                    .append(DATA_SEPARATOR)
                    .append(this.materialDragged.getThickness());
            event.data = builder.toString();
            System.out.println("... dragSetData ...\t"+builder.toString());
        }
        System.out.println("... dragSetData ... selection vaccia ...");

	}

	@Override
	public void dragFinished(DragSourceEvent event) {
        System.out.println("... dragFinishes ...");

		//this.data.remove(this.materialDragged);
        this.materialDragged = null;

        // clear selection
        this.selectionLayer.clear();

        this.natTable.refresh();
		
	}


	@Override
	public void dragEnter(DropTargetEvent event) {
		event.detail = DND.DROP_COPY;
		
	}


	@Override
	public void dragLeave(DropTargetEvent event) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void dragOver(DropTargetEvent event) {
		// TODO Auto-generated method stub
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public void drop(DropTargetEvent event) {
        String[] data = (event.data != null ? event.data.toString().split(
                "\\" + DATA_SEPARATOR) : new String[] {});
        if (data.length > 0) {
        	if(data[1].equals("Composite")) {
        		
        	}
            AncolabStackingLayer p = new AncolabStackingLayer(data[0], data[1],  data[2], data[3] );
 
            int rowPosition = getRowPosition(event);
            if (rowPosition > 0) {
                this.data.add(rowPosition - 1, p);
            } else {
                this.data.add(p);
            }
            this.natTable.refresh();
        }
		
	}


	@Override
	public void dropAccept(DropTargetEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	private int getRowPosition(DropTargetEvent event) {
        Point pt = event.display.map(null, this.natTable, event.x, event.y);
        int position = this.natTable.getRowPositionByY(pt.y);
        return position;
    }

}
