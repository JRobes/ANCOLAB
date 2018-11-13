package materialstable;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.filterrow.DefaultGlazedListsFilterStrategy;
import org.eclipse.nebula.widgets.nattable.filterrow.FilterRowHeaderComposite;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import nattable05filter.adddeleterows.MenuConfigurationMaterialsTable;
import nattable05filter.parts.DragAndDropSupport;

@SuppressWarnings("restriction")
public class MaterialsTable {
	@SuppressWarnings("unused")
	@Inject 
	private EventBroker broker;
    private ListDataProvider<AncolabMaterial> bodyDataProvider;
    private SelectionLayer selectionLayer;
    private MaterialsTableBodyLayerStack bodyLayer;
    
    private NatTable natTable;
    
	@SuppressWarnings("unchecked")
	public MaterialsTable(Composite parent, List<AncolabMaterial> values) {
        ConfigRegistry configRegistry = new ConfigRegistry();
        String[] propertyNames = { "name","library", "type", "thickness", "e1", "e2", "g12","nu12"};
        String[] materialTableColumnNames = new String[]{"Material Name", "Library",  "Type", "Thickness", "E1", "E2", "G12", "nu12"};

		IColumnPropertyAccessor<AncolabMaterial> columnPropertyAccessor  = new ReflectiveColumnPropertyAccessor<AncolabMaterial>(propertyNames);

		//BODYLAYER STACK
		MaterialsTableBodyLayerStack bodyLayerStack = new MaterialsTableBodyLayerStack(values, columnPropertyAccessor);		
        this.bodyLayer = bodyLayerStack;
        bodyLayerStack.getEventList().remove(0);
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
        natTable = new NatTable(parent, gridLayer, false);
        // as the autoconfiguration of the NatTable is turned off, we have to
        // add the DefaultNatTableStyleConfiguration and the ConfigRegistry
        // manually
        natTable.setConfigRegistry(configRegistry);
        natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        // add filter row configuration
        natTable.addConfiguration(new MaterialsTableFilterRowConfiguration());
        // add menu configuration
        natTable.addConfiguration(new MenuConfigurationMaterialsTable(natTable));
        
        natTable.configure();
        //ADD D&D SUPPORT
        DragAndDropSupport dragSupport = 
                new DragAndDropSupport(natTable, bodyLayerStack.getSelectionLayer(), bodyLayerStack.getFilterList());
        Transfer[] transfer = { TextTransfer.getInstance() };
        natTable.addDragSupport(DND.DROP_COPY, transfer, dragSupport);        
        
        this.selectionLayer = bodyLayerStack.getSelectionLayer();
        
        this.bodyDataProvider = (ListDataProvider<AncolabMaterial>) bodyLayerStack.getBodyDataProvider();

 
        
    }
	
    public NatTable getNatTable() {
		return natTable;
	}
	public ListDataProvider<AncolabMaterial> getBodyDataProvider() {
		return this.bodyDataProvider;
	}

	public SelectionLayer getSelectionLayer() {
		return selectionLayer;
	}

	public MaterialsTableBodyLayerStack getBodyLayerStack() {
		return bodyLayer;
	}



    }
    


