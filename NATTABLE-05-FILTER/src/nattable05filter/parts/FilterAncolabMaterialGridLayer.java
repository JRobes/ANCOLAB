package nattable05filter.parts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;
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
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.stack.DefaultBodyLayerStack;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionModel;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultRowSelectionLayerConfiguration;

import aero.alestis.stresstools.ancolab.business.FacadeBusinessAncolabMaterials;
import aero.alestis.stresstools.ancolab.db.FacadeDbAncolabMaterials.LoadSaveStrategy;
import aero.alestis.stresstools.ancolab.model.AncolabConstants;
import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;

public class FilterAncolabMaterialGridLayer extends GridLayer {

    private final ListDataProvider<AncolabMaterial> bodyDataProvider;
    private SelectionLayer selectionLayer;
    private List<AncolabMaterial> listOfData;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public FilterAncolabMaterialGridLayer(IConfigRegistry configRegistry) {
        super(true);
       // parseAncolabMaterials();

        // Underlying data source
        //this.listOfData = parseAncolabMaterials();

        this.listOfData = FacadeBusinessAncolabMaterials.INSTANCE.getAllMaterials(LoadSaveStrategy.SERIALIZABLE, 
        		"C:\\Users\\javier.robes\\Desktop\\TAREAS\\ANCOLAB-MATERIALS\\serial.ser");
        
        //TEST
        FacadeBusinessAncolabMaterials.INSTANCE.saveAllMaterials(LoadSaveStrategy.SERIALIZABLE, 
        		"C:\\Users\\javier.robes\\Desktop\\serial.ser",
        		listOfData);
        
        
        EventList<AncolabMaterial> eventList = GlazedLists.eventList(listOfData);
        FilterList<AncolabMaterial> filterList = new FilterList<>(eventList);
        
        String[] propertyNames = { "name","library", "type", "thickness", "e1", "e2", "g12","nu12"};
		IColumnPropertyAccessor columnPropertyAccessor  = new ReflectiveColumnPropertyAccessor<AncolabMaterial>(propertyNames);

        // Body layer
        this.bodyDataProvider = new ListDataProvider<>(filterList, columnPropertyAccessor);
        DataLayer bodyDataLayer = new DataLayer(this.bodyDataProvider);
        bodyDataLayer.setColumnWidthByPosition(0, AncolabConstants.MAT_NAME_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(1, AncolabConstants.STRING_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(2, AncolabConstants.STRING_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(3, AncolabConstants.STRING_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(4, AncolabConstants.NUMBER_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(5, AncolabConstants.NUMBER_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(6, AncolabConstants.NUMBER_CELL_WIDTH);
        bodyDataLayer.setColumnWidthByPosition(7, AncolabConstants.NUMBER_CELL_WIDTH);


        //bodyDataLayer.setColumnPositionResizable(0, false);
        bodyDataLayer.setColumnsResizableByDefault(false);
        DefaultBodyLayerStack bodyLayer = new DefaultBodyLayerStack(bodyDataLayer);
        
        this.selectionLayer = bodyLayer.getSelectionLayer();
        // set row selection model with single selection enabled
        bodyLayer.getSelectionLayer().setSelectionModel(new RowSelectionModel(
        		bodyLayer.getSelectionLayer(),
                bodyDataProvider,
                new IRowIdAccessor() {
					@Override
					public Serializable getRowId(Object rowObject) {
						
						return ((AncolabMaterial) rowObject).getName();
					}

                },
                false));
        this.selectionLayer.addConfiguration(new DefaultRowSelectionLayerConfiguration());

        
        ColumnOverrideLabelAccumulator bodyLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
        bodyDataLayer.setConfigLabelAccumulator(bodyLabelAccumulator);
        
        //ESTO NO SE QUE ES
       // bodyLabelAccumulator.registerColumnOverrides(
        //      RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.PRICING_TYPE_PROP_NAME),
        //        "PRICING_TYPE_PROP_NAME");

        // Column header layer
        String[] materialTableColumnNames = new String[]{"Material Name", "Library",  "Type", "Thickness", "E1", "E2", "G12", "nu12"};
        IDataProvider columnHeaderDataProvider =
                new DefaultColumnHeaderDataProvider(materialTableColumnNames);
        DataLayer columnHeaderDataLayer =
                new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        ColumnHeaderLayer columnHeaderLayer =
                new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayer, bodyLayer.getSelectionLayer());

        // Note: The column header layer is wrapped in a filter row composite.
        // This plugs in the filter row functionality
        FilterRowHeaderComposite<AncolabMaterial> filterRowHeaderLayer =
                new FilterRowHeaderComposite<>(
                        new DefaultGlazedListsFilterStrategy<>(filterList, columnPropertyAccessor, configRegistry),
                        columnHeaderLayer, columnHeaderDataProvider, configRegistry);

        ColumnOverrideLabelAccumulator labelAccumulator =
                new ColumnOverrideLabelAccumulator(columnHeaderDataLayer);
        columnHeaderDataLayer.setConfigLabelAccumulator(labelAccumulator);

        // Register labels
      //  labelAccumulator.registerColumnOverrides(
       //         RowDataListFixture.getColumnIndexOfProperty(RowDataListFixture.RATING_PROP_NAME),
        //        "CUSTOM_COMPARATOR_LABEL");

        // Row header layer
        DefaultRowHeaderDataProvider rowHeaderDataProvider =
                new DefaultRowHeaderDataProvider(this.bodyDataProvider);
        DefaultRowHeaderDataLayer rowHeaderDataLayer =
                new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        //rowHeaderDataLayer.set
        RowHeaderLayer rowHeaderLayer =
                new RowHeaderLayer(rowHeaderDataLayer, bodyLayer, bodyLayer.getSelectionLayer());

        // Corner layer
        DefaultCornerDataProvider cornerDataProvider =
                new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        DataLayer cornerDataLayer =
                new DataLayer(cornerDataProvider);
        CornerLayer cornerLayer =
                new CornerLayer(cornerDataLayer, rowHeaderLayer, filterRowHeaderLayer);

        // Grid
        setBodyLayer(bodyLayer);
        // Note: Set the filter row as the column header
        setColumnHeaderLayer(filterRowHeaderLayer);
        setRowHeaderLayer(rowHeaderLayer);
        setCornerLayer(cornerLayer);
    }

	@SuppressWarnings("unchecked")
	private List<AncolabMaterial> parseAncolabMaterials() {
		List<AncolabMaterial>listOfMaterials  = new ArrayList<AncolabMaterial>();
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("C:\\Users\\javier.robes\\Desktop\\TAREAS\\ANCOLAB-MATERIALS\\serial.ser"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			listOfMaterials= (List<AncolabMaterial>) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		System.out.println(listOfMaterials.size());
		return listOfMaterials;		
	}

	public SelectionLayer getSelectionLayer() {
		return selectionLayer;
	}
	public List<AncolabMaterial> getAncolabMaterialData(){
		return this.listOfData;
		
	}
	public IDataProvider getDataProvider() {
		return bodyDataProvider;
	}

	
}
