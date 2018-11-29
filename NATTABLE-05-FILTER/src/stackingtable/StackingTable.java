package stackingtable;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.AggregateConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.CellOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import aero.alestis.stresstools.ancolab.model.AncolabConstants;
import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;
import ca.odell.glazedlists.EventList;
import nattable05filter.parts.DragAndDropSupport;

public class StackingTable {
	private NatTable natTable;
    private static IDataProvider bodyDataProvider;
 

	public static ISelectionProvider selectionProvider;

    public static String COLUMN_ONE_LABEL = "ColumnOneLabel";
    public static String COLUMN_TWO_LABEL = "ColumnTwoLabel";
    public static String COLUMN_THREE_LABEL = "ColumnThreeLabel";
    public static String COLUMN_FOUR_LABEL = "ColumnFourLabel";
    public static String COLUMN_FIVE_LABEL = "ColumnFiveLabel";
    public static String TEST = "FFF";
	public static String INVALID_THICKNESS_LABEL = "InvalidThicknessLabel";
	public static String INVALID_ANGLE_LABEL = "InvalidAngleLabel";

	
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public StackingTable(Composite parent, 
			EventList<AncolabStackingLayer> ancolabStackingData, 
			SelectionLayer stackingTableSelectionLayer ) {
    	
        IConfigRegistry configRegistry = null;
    	String[] propertyNames = { "name", "library", "type", "angle", "thickness"};
        // mapping from property to label, needed for column header labels
		Map propertyToLabelMap = new HashMap<>();
        propertyToLabelMap.put("name", "Material Name");
        propertyToLabelMap.put("library", "Library");
        propertyToLabelMap.put("type", "Type");
        propertyToLabelMap.put("angle", "Angle");
        propertyToLabelMap.put("thickness", "Thickness");
		IColumnPropertyAccessor columnPropertyAccessor = new ReflectiveColumnPropertyAccessor<>(propertyNames);
        bodyDataProvider = new ListDataProvider<>(ancolabStackingData, columnPropertyAccessor);

        final DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        bodyDataLayer.setColumnWidthByPosition(0, AncolabConstants.MAT_NAME_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(1, AncolabConstants.STRING_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(2, AncolabConstants.STRING_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(3, AncolabConstants.NUMBER_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(4, AncolabConstants.STRING_CELL_WIDTH);


        
        stackingTableSelectionLayer = new SelectionLayer(bodyDataLayer);
        ViewportLayer viewportLayer = new ViewportLayer(stackingTableSelectionLayer);
       
        // create the column header layer stack
        IDataProvider columnHeaderDataProvider =  new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
        ILayer columnHeaderLayer = new ColumnHeaderLayer(new DataLayer(columnHeaderDataProvider), viewportLayer, stackingTableSelectionLayer);

        // create the row header layer stack
        DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
        ILayer rowHeaderLayer = new RowHeaderLayer(new DataLayer(rowHeaderDataProvider, 40, AncolabConstants.ROW_HEIGHT), viewportLayer, stackingTableSelectionLayer);

        // create the corner layer stack
        ILayer cornerLayer = new CornerLayer(
        		new DataLayer(new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider)),
                rowHeaderLayer,
                columnHeaderLayer);

        // create the grid layer composed with the prior created layer stacks
        GridLayer gridLayer = new GridLayer(viewportLayer, columnHeaderLayer, rowHeaderLayer, cornerLayer);
        natTable = new NatTable(parent,  gridLayer, false);
        natTable.setBackgroundMode(SWT.NO_BACKGROUND);
        natTable.setSize(natTable.getPreferredWidth(), natTable.getPreferredHeight());
       selectionProvider = new RowSelectionProvider<AncolabStackingLayer>(stackingTableSelectionLayer,(IRowDataProvider<AncolabStackingLayer>) bodyDataProvider,false);

       NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable,  DataLayer.DEFAULT_ROW_HEIGHT);
       natTable.setLayerPainter(layerPainter);   
       
       
       

       final ColumnOverrideLabelAccumulator columnLabelAccumulator =
               new ColumnOverrideLabelAccumulator(bodyDataLayer);
       registerColumnLabels(columnLabelAccumulator);      
       
       
       //final CellOverrideLabelAccumulator cellOverrideLabelAccumulator = 
    	//	   new CellOverrideLabelAccumulator((IRowDataProvider) bodyDataLayer);
       
       //cellOverrideLabelAccumulator.registerOverride(cellValue, col, configLabel);
       
       
       AggregateConfigLabelAccumulator aggregateConfigLabelAccumulator = new AggregateConfigLabelAccumulator();
       aggregateConfigLabelAccumulator.add(columnLabelAccumulator);
       aggregateConfigLabelAccumulator.add(new StackingTableLabelAccumulator(bodyDataProvider));
       aggregateConfigLabelAccumulator.add(new StackingTableThicknessLabelAccumulator(bodyDataProvider));
       aggregateConfigLabelAccumulator.add(new StackingTableOrientationLabelAccumulator(bodyDataProvider));

       
       bodyDataLayer.setConfigLabelAccumulator(aggregateConfigLabelAccumulator);
       

      // StackingTableLabelAccumulator acc2 = new StackingTableLabelAccumulator(bodyDataProvider);
       
      // acc2.registerOverrides(key, configLabels);
       
       //SI ELIMINO LO DE ABAJO NO SE VEN LOS DATOS
       /*
       natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
       gridLayer.addConfiguration(new DefaultEditConfiguration());
       gridLayer.addConfiguration(new DefaultEditBindings());
     */
       bodyDataLayer.addConfiguration(new DefaultNatTableStyleConfiguration());
       bodyDataLayer.addConfiguration(new StackingTableConfiguration(bodyDataProvider));

       bodyDataLayer.addConfiguration(new DefaultEditConfiguration());
       bodyDataLayer.addConfiguration(new DefaultEditBindings());   
       
       natTable.configure();    
       
       DragAndDropSupport dropSupport =
               new DragAndDropSupport(natTable, stackingTableSelectionLayer, ancolabStackingData);
       Transfer[] transfer2 = { TextTransfer.getInstance() };
       natTable.addDropSupport(DND.DROP_COPY, transfer2, dropSupport);
      
		 
		 //LABEL ACCUMULATORS
      // CellOverrideLabelAccumulator cellLabelAccumulator =
        //       new CellOverrideLabelAccumulator<>(bodyDataProvider);
       
      // cellLabelAccumulator.registerOverride("AAA", 2, CELL_LABEL);
		 //END LABEL ACCUMULATORS

      
	}
	
	public NatTable getNatTable() {
		return natTable;
	}
	public static IDataProvider getBodyDataProvider() {
		return bodyDataProvider;
	}
    private static void registerColumnLabels(ColumnOverrideLabelAccumulator columnLabelAccumulator) {
        columnLabelAccumulator.registerColumnOverrides(0, COLUMN_ONE_LABEL);
        columnLabelAccumulator.registerColumnOverrides(1, COLUMN_TWO_LABEL);
        columnLabelAccumulator.registerColumnOverrides(2, COLUMN_THREE_LABEL);
        columnLabelAccumulator.registerColumnOverrides(3, COLUMN_FOUR_LABEL);
        columnLabelAccumulator.registerColumnOverrides(4, COLUMN_FIVE_LABEL);
        //columnLabelAccumulator.registerColumnOverrides(4, TEST);

    }
	
	
}
