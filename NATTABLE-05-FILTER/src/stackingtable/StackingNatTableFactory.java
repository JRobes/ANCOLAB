package stackingtable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;
import org.eclipse.nebula.widgets.nattable.data.validate.IDataValidator;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.Style;
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


public class StackingNatTableFactory {
    public static String COLUMN_ONE_LABEL = "ColumnOneLabel";
    public static String COLUMN_TWO_LABEL = "ColumnTwoLabel";
    public static String COLUMN_THREE_LABEL = "ColumnThreeLabel";
    public static String COLUMN_FOUR_LABEL = "ColumnFourLabel";
    public static String COLUMN_FIVE_LABEL = "ColumnFiveLabel";
    
    public static String COMPOSITE_THICKNESS_LABEL = "CompositeThicknessLabel";
    
    
    public static IDataProvider bodyDataProvider;
    public static ISelectionProvider selectionProvider;
    
    private StackingNatTableFactory() {
    	
    }
    
    @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public static NatTable createTable(Composite parent, EventList<AncolabStackingLayer> ancolabStackingData, SelectionLayer stackingTableSelectionLayer) {
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
        
        final ColumnOverrideLabelAccumulator columnLabelAccumulator =
                new ColumnOverrideLabelAccumulator(bodyDataLayer);
        bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);
        registerColumnLabels(columnLabelAccumulator);      
        
        stackingTableSelectionLayer = new SelectionLayer(bodyDataLayer);
        ViewportLayer viewportLayer = new ViewportLayer(stackingTableSelectionLayer);
        
        //viewportLayer.setHorizontalScrollbarEnabled(true);
        
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
        NatTable natTable = new NatTable(parent,  gridLayer, false);
        natTable.setBackgroundMode(SWT.NO_BACKGROUND);
        natTable.setSize(natTable.getPreferredWidth(), natTable.getPreferredHeight());
       // NatTable natTable = new NatTable(parent,  gridLayer);

       selectionProvider = new RowSelectionProvider<AncolabStackingLayer>(stackingTableSelectionLayer,(IRowDataProvider<AncolabStackingLayer>) bodyDataProvider,false);
        
        
        /*
        		stackingTableSelectionLayer, bodyDataProvider ,
                false); // Provides rows where any cell in the row is selected
        */
        
        NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable,  DataLayer.DEFAULT_ROW_HEIGHT);
        
        natTable.setLayerPainter(layerPainter);            
       
        //bodyDataLayer.addConfiguration(new DefaultEditConfiguration());
        //bodyDataLayer.addConfiguration(new DefaultEditBindings());
        
        //natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
       // bodyDataLayer.addConfiguration(new DefaultGridLayerConfiguration(gridLayer));
       
  /*      
        natTable.addConfiguration(new AbstractRegistryConfiguration() {

            @Override
            public void configureRegistry(IConfigRegistry configRegistry) {
                configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE,
                        IEditableRule.ALWAYS_EDITABLE);
            }
        });

 */     
        //SI ELIMINO LO DE ABAJO NO SE VEN LOS DATOS
        
        
        natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        gridLayer.addConfiguration(new DefaultEditConfiguration());
        gridLayer.addConfiguration(new DefaultEditBindings());
        natTable.addConfiguration(new AbstractRegistryConfiguration() {

            @Override
            public void configureRegistry(IConfigRegistry configRegistry) {
            	Style cellStyle = new Style();
                cellStyle.setAttributeValue(
                        CellStyleAttributes.BACKGROUND_COLOR,
                        GUIHelper.COLOR_WHITE);
                configRegistry.registerConfigAttribute(
                        CellConfigAttributes.CELL_STYLE, cellStyle,
                        DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_ONE_LABEL);
                configRegistry.registerConfigAttribute(
                        CellConfigAttributes.CELL_STYLE, cellStyle,
                        DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_TWO_LABEL);
                configRegistry.registerConfigAttribute(
                        CellConfigAttributes.CELL_STYLE, cellStyle,
                        DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_THREE_LABEL);
                configRegistry.registerConfigAttribute(
                        CellConfigAttributes.CELL_STYLE, cellStyle,
                        DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_FOUR_LABEL);
                configRegistry.registerConfigAttribute(
                        CellConfigAttributes.CELL_STYLE, cellStyle,
                        DisplayMode.NORMAL, StackingNatTableFactory.COLUMN_FIVE_LABEL);


            	configRegistry.registerConfigAttribute(
                		EditConfigAttributes.CELL_EDITABLE_RULE, 
                		//new OrientationEditableRule(selectionProvider, StressToolsConstants.HONEYCOMB_COMPOSITE_TYPE),
                		IEditableRule.ALWAYS_EDITABLE, 
                		DisplayMode.EDIT, 
                		StackingNatTableFactory.COLUMN_FIVE_LABEL);
            	/*
            	configRegistry.registerConfigAttribute(
                		EditConfigAttributes.CELL_EDITABLE_RULE, 
                		new OrientationEditableRule(selectionProvider, StressToolsConstants.HONEYCOMB_COMPOSITE_TYPE),
                		//IEditableRule.ALWAYS_EDITABLE, 
                		DisplayMode.EDIT, 
                		StackingNatTableFactory.COLUMN_FOUR_LABEL);
            	*/
            	
            	
            	
            	/* ESTO TENGO QUE CAMBIARLOS PORQUE BORRE LA PUTA CLASE THICKNESSEDITABLE RULE
            	 * TEGO QUE VOLER A HACERLA....CAGONDIOS...
            	configRegistry.registerConfigAttribute(
                		EditConfigAttributes.CELL_EDITABLE_RULE, 
                		new ThicknessEditableRule(selectionProvider, "Honeycomb", 4),
                		//IEditableRule.ALWAYS_EDITABLE, 
                		DisplayMode.EDIT, 
                		StackingNatTableFactory.COLUMN_FIVE_LABEL);
                		
                */
            	
            	
            	//TextCellEditor textCellEditor = new TextCellEditor();
            	//textCellEditor.setErrorDecorationEnabled(true);
            	
            	
 /*               
                configRegistry.registerConfigAttribute(
                		EditConfigAttributes.CELL_EDITOR, 
                		new TextCellEditor(true, true),
                        DisplayMode.EDIT,
                        NatTableFactory.COLUMN_FIVE_LABEL);
                
                configRegistry.registerConfigAttribute(
                        EditConfigAttributes.DATA_VALIDATOR,
                        getThicknessValidator(), DisplayMode.EDIT,
                        NatTableFactory.COLUMN_FIVE_LABEL);
 */           

             /*
                configRegistry.registerConfigAttribute(
                        EditConfigAttributes.DATA_VALIDATOR,
                        getOrientationValidator(), DisplayMode.EDIT,
                        NatTableFactory.COLUMN_TWO_LABEL);
            */
                
                registerOrientationValidator(configRegistry);
            }
            
            
            
            
        });
 
       
        natTable.configure();    
        
        DragAndDropSupport dropSupport =
                new DragAndDropSupport(natTable, stackingTableSelectionLayer, ancolabStackingData);
        Transfer[] transfer2 = { TextTransfer.getInstance() };
        natTable.addDropSupport(DND.DROP_COPY, transfer2, dropSupport);
       
        natTable.setBackground(GUIHelper.COLOR_WIDGET_LIGHT_SHADOW);
       
    	return natTable;
    	
    }

    
    private static void registerOrientationValidator(IConfigRegistry configRegistry) {

        TextCellEditor textCellEditor = new TextCellEditor();
        textCellEditor.setErrorDecorationEnabled(true);
        textCellEditor.setErrorDecorationText(
                "ZZZZZZZ");
        textCellEditor.setDecorationPositionOverride(SWT.LEFT | SWT.TOP);
        configRegistry.registerConfigAttribute(
                EditConfigAttributes.CELL_EDITOR, textCellEditor,
                DisplayMode.EDIT, COLUMN_FIVE_LABEL);

        configRegistry.registerConfigAttribute(
                EditConfigAttributes.DATA_VALIDATOR, getOrientationValidator(),
                DisplayMode.EDIT, COLUMN_FIVE_LABEL);
    }
    
    private static void registerColumnLabels(ColumnOverrideLabelAccumulator columnLabelAccumulator) {
        columnLabelAccumulator.registerColumnOverrides(0, COLUMN_ONE_LABEL);
        columnLabelAccumulator.registerColumnOverrides(1, COLUMN_TWO_LABEL);
        columnLabelAccumulator.registerColumnOverrides(2, COLUMN_THREE_LABEL);
        columnLabelAccumulator.registerColumnOverrides(3, COLUMN_FOUR_LABEL);
        columnLabelAccumulator.registerColumnOverrides(4, COLUMN_FIVE_LABEL);
    }
    
    @SuppressWarnings("unused")
	private static IDataValidator getThicknessValidator() {
        return new DataValidator() {
			@Override
			public boolean validate(int columnIndex, int rowIndex, Object newValue) {
				try
				{
				    Number n =  NumberFormat.getInstance().parse((String) newValue);
				    if(n.doubleValue() > 0) 
				    	return true;
				    return false;
				}
				catch(ParseException e)
				{
					return false;
				}
			}
        };
    }
    
    private static IDataValidator getOrientationValidator() {
        return new DataValidator() {

			@Override
			public boolean validate(int columnIndex, int rowIndex, Object newValue) {
				
				return isNumericArray((String) newValue);
			}
        	
        };
    }
    
    
    
    private static boolean isNumericArray(String str) {
        if (str == null)
            return false;
        char[] data = str.toCharArray();
        if (data.length <= 0)
            return false;
        int index = 0;
        if (data[0] == '-' && data.length > 1)
            index = 1;
        for (; index < data.length; index++) {
            if (data[index] < '0' || data[index] > '9') // Character.isDigit() can go here too.
                return false;
        }
        return true;
    }


    

}
