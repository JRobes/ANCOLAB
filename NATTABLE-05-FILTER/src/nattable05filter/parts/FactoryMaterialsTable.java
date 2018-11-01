package nattable05filter.parts;

import static org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes.CELL_PAINTER;
import static org.eclipse.nebula.widgets.nattable.grid.GridRegion.FILTER_ROW;
import static org.eclipse.nebula.widgets.nattable.style.DisplayMode.NORMAL;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommandHandler;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.DefaultGlazedListsFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterIconPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowDataLayer;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowPainter;
import org.eclipse.nebula.widgets.nattable.filterrow.config.FilterRowConfigAttributes;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TransformedList;
import nattable05filter.adddeleterows.MenuConfigurationMaterialsTable;
import nattable05filter.adddeleterows.AddRowCommandHandler;
import nattable05filter.adddeleterows.DeleteRowCommandHandler;

public class FactoryMaterialsTable {

    private ListDataProvider<AncolabMaterial> bodyDataProvider;
    private SelectionLayer selectionLayer;
    private List<AncolabMaterial> listOfData;
	
	
	public static NatTable createTable(Composite parent, List<AncolabMaterial> values) {
        ConfigRegistry configRegistry = new ConfigRegistry();
        String[] propertyNames = { "name","library", "type", "thickness", "e1", "e2", "g12","nu12"};
        String[] materialTableColumnNames = new String[]{"Material Name", "Library",  "Type", "Thickness", "E1", "E2", "G12", "nu12"};

		IColumnPropertyAccessor<AncolabMaterial> columnPropertyAccessor  = new ReflectiveColumnPropertyAccessor<AncolabMaterial>(propertyNames);

		//BODYLAYER STACK
        BodyLayerStack bodyLayerStack = new BodyLayerStack(values, columnPropertyAccessor);		
        
        
        //COLUMN HEADER LAYER
        IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(materialTableColumnNames);        
        DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);       
        ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack, bodyLayerStack.getSelectionLayer());       
		
        // Note: The column header layer is wrapped in a filter row composite.
        // This plugs in the filter row functionality
        @SuppressWarnings("rawtypes")
		FilterRowHeaderComposite filterRowHeaderLayer = new FilterRowHeaderComposite<>(
                        new DefaultGlazedListsFilterStrategy<>(bodyLayerStack.getFilterList(), columnPropertyAccessor, configRegistry),
                        columnHeaderLayer,
                        columnHeaderDataLayer.getDataProvider(),
                        configRegistry);       
 
        ColumnOverrideLabelAccumulator labelAccumulator = new ColumnOverrideLabelAccumulator(columnHeaderDataLayer);
        columnHeaderDataLayer.setConfigLabelAccumulator(labelAccumulator);
        
        //ROW HEADER LAYER
        IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyLayerStack.getBodyDataProvider());
        DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayerStack, bodyLayerStack.getSelectionLayer());
        
        //CORNER LAYER
        IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
        ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, filterRowHeaderLayer);
        
        //BUILD GRID LAYER
        GridLayer gridLayer = new GridLayer(bodyLayerStack, filterRowHeaderLayer, rowHeaderLayer, cornerLayer);

        // turn the auto configuration off as we want to add our header menu
        // configuration
        NatTable natTable = new NatTable(parent, gridLayer, false);
        // as the autoconfiguration of the NatTable is turned off, we have to
        // add the DefaultNatTableStyleConfiguration and the ConfigRegistry
        // manually
        natTable.setConfigRegistry(configRegistry);
        natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        // add filter row configuration
        natTable.addConfiguration(new FilterRowConfiguration());
        // add menu configuration
        natTable.addConfiguration(new MenuConfigurationMaterialsTable(natTable));
        
        natTable.configure();
        //ADD D&D SUPPORT
        DragAndDropSupport dragSupport = 
                new DragAndDropSupport(natTable, bodyLayerStack.getSelectionLayer(), bodyLayerStack.getFilterList());
        Transfer[] transfer = { TextTransfer.getInstance() };
        natTable.addDragSupport(DND.DROP_COPY, transfer, dragSupport);        
		return natTable;
	}

	/**
     * Always encapsulate the body layer stack in an AbstractLayerTransform to
     * ensure that the index transformations are performed in later commands.
     *
     * @param 
     */
    static class BodyLayerStack extends AbstractLayerTransform {

        private final EventList<AncolabMaterial> filterList;

        private final IDataProvider bodyDataProvider;

        private final SelectionLayer selectionLayer;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public BodyLayerStack(List<AncolabMaterial> values, IColumnPropertyAccessor<AncolabMaterial> columnPropertyAccessor) {
            DataLayer dp = new DataLayer((IDataProvider) values);
            //dp.getdat.getda
			// wrapping of the list to show into GlazedLists
            // see http://publicobject.com/glazedlists/ for further information
            EventList<AncolabMaterial> eventList = GlazedLists.eventList(values);
            DataLayer dataLayer = new DataLayer((IDataProvider) eventList);
            dataLayer.registerCommandHandler(new AddRowCommandHandler(eventList));
            TransformedList<?, ?> rowObjectsGlazedList = GlazedLists.threadSafeList(eventList);
            // use the SortedList constructor with 'null' for the Comparator
            // because the Comparator will be set by configuration
            SortedList<?> sortedList = new SortedList<>(rowObjectsGlazedList, null);
            // wrap the SortedList with the FilterList
            this.filterList = new FilterList<AncolabMaterial>((EventList<AncolabMaterial>) sortedList);
           // this.filterList.get
            
            //this.bodyDataProvider = new ListDataProvider<AncolabMaterial>(eventList, columnPropertyAccessor);
            this.bodyDataProvider = new ListDataProvider<AncolabMaterial>(filterList, columnPropertyAccessor);
            
            DataLayer bodyDataLayer = new DataLayer(getBodyDataProvider());
            bodyDataLayer.registerCommandHandler(new DeleteRowCommandHandler<>(((ListDataProvider<AncolabMaterial>) bodyDataProvider).getList()));
            //bodyDataLayer.registerCommandHandler(new AddRowCommandHandler(((ListDataProvider<AncolabMaterial>) bodyDataProvider).getList()));
            //bodyDataLayer.registerCommandHandler(new AddRowCommandHandler(eventList));

            //Set cell width
            bodyDataLayer.setColumnWidthByPosition(0, 250);
            bodyDataLayer.setColumnWidthByPosition(1,  90);
            bodyDataLayer.setColumnWidthByPosition(2,  90);
            bodyDataLayer.setColumnWidthByPosition(3,  90);
            bodyDataLayer.setColumnWidthByPosition(4,  65);
            bodyDataLayer.setColumnWidthByPosition(5,  65);
            bodyDataLayer.setColumnWidthByPosition(6,  65);
            bodyDataLayer.setColumnWidthByPosition(7,  65);
            
            bodyDataLayer.setColumnsResizableByDefault(false);
            
            ColumnOverrideLabelAccumulator bodyLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
            bodyDataLayer.setConfigLabelAccumulator(bodyLabelAccumulator);
           
            // layer for event handling of GlazedLists and PropertyChanges
            GlazedListsEventLayer<?> glazedListsEventLayer =
                    new GlazedListsEventLayer<AncolabMaterial>(bodyDataLayer, this.filterList);
           // glazedListsEventLayer.
            
            glazedListsEventLayer.registerCommandHandler(new AddRowCommandHandler(((ListDataProvider<AncolabMaterial>) bodyDataProvider).getList()));
            //this.selectionLayer = new SelectionLayer(glazedListsEventLayer);
            
            this.selectionLayer = new SelectionLayer(glazedListsEventLayer, false);
 
            // use a RowSelectionModel that will perform row selections and is able
            // to identify a row via unique ID
            selectionLayer.setSelectionModel(new RowSelectionModel<>(
                    selectionLayer, (IRowDataProvider) bodyDataProvider, new IRowIdAccessor() {
						@Override
						public Serializable getRowId(Object rowObject) {
                            return ((AncolabMaterial)rowObject).getName();
						}

                    },false));
            selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());

            ViewportLayer viewportLayer = new ViewportLayer(getSelectionLayer());
            setUnderlyingLayer(viewportLayer);
        }

        public SelectionLayer getSelectionLayer() {
            return this.selectionLayer;
        }

        public FilterList<AncolabMaterial> getFilterList() {
            return (FilterList<AncolabMaterial>) this.filterList;
        }

        public IDataProvider getBodyDataProvider() {
            return this.bodyDataProvider;
        }
    }
    
    /**
     * The configuration to enable the edit mode for the grid and additional
     * edit configurations like converters and validators.
     */
    static class FilterRowConfiguration extends AbstractRegistryConfiguration {

        @Override
        public void configureRegistry(IConfigRegistry configRegistry) {
        	// override the default filter row configuration for painter
        	configRegistry.registerConfigAttribute(
                    CELL_PAINTER,
                    new FilterRowPainter(new FilterIconPainter(GUIHelper.getImage("filter"))),
                    NORMAL,
                    FILTER_ROW);
            // Register a combo box editor to be displayed in the filter row
            // cell when a value is selected from the combo, the object is
            // converted to a string using the converter (registered below)
            configRegistry.registerConfigAttribute(
                    EditConfigAttributes.CELL_EDITOR,
                    new ComboBoxCellEditor(Arrays.asList("Composite", "Honeycomb")),
                    DisplayMode.NORMAL,
                    FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);
            // The pricing bean object in column is converted to using this
            // display converter
            // A 'text' match is then performed against the value from the combo
            // box
            configRegistry.registerConfigAttribute(
                    FilterRowConfigAttributes.FILTER_DISPLAY_CONVERTER,
                    new MaterialTypeBeanDisplayConverter(),
                    DisplayMode.NORMAL,
                    FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);

            configRegistry.registerConfigAttribute(
                    CellConfigAttributes.DISPLAY_CONVERTER,
                    new MaterialTypeBeanDisplayConverter(),
                    DisplayMode.NORMAL,
                    FilterRowDataLayer.FILTER_ROW_COLUMN_LABEL_PREFIX + 2);
        
        
        
        }
    }
}























