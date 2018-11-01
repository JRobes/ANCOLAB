package nattable05filter.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;

import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;

import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import aero.alestis.stresstools.ancolab.model.AncolabConstants;
import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

public class SamplePart {
	
    final EventList<AncolabStackingLayer> ancolabStackingData = GlazedLists.eventListOf();
    SelectionLayer stackingTableSelectionLayer;
	private Text txtInput;
	private  NatTable natTableStacking;
	private  NatTable natTableMaterials;

	@Inject
	private MDirtyable dirty;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void createComposite(Composite parent ) {
		//parent.setLayout(new GridLayout(1, false));
		ancolabStackingData.add(new AncolabStackingLayer("sdf","ffff","gggg", "www"));
		parent.setLayout(new GridLayout(AncolabConstants.GRIDLAYOUT_NUM_COLUMNS, false));
		txtInput = new Text(parent, SWT.BORDER);
		txtInput.setMessage("Enter text to mark part as dirty");
		txtInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		GridData gridDataTxtInput = new GridData(GridData.FILL_HORIZONTAL);
		gridDataTxtInput.horizontalSpan = AncolabConstants.GRIDLAYOUT_NUM_COLUMNS;
		txtInput.setLayoutData(gridDataTxtInput);

		Label stackingLabel = new Label(parent, 0);
		stackingLabel.setText("Stacking Table");
		GridData gridDataLabelStackingTable = new GridData();
		gridDataLabelStackingTable.horizontalSpan = 1;
		stackingLabel.setLayoutData(gridDataLabelStackingTable);	
		
		Label materialsLabel = new Label(parent, 0);
		materialsLabel.setText("Materials Table");
		GridData gridDataLabelMaterialsTable = new GridData();
		gridDataLabelMaterialsTable.horizontalSpan = 3;
		stackingLabel.setLayoutData(gridDataLabelMaterialsTable);			
		
		
		
        @SuppressWarnings("rawtypes")
		ListEventListener listChangeListener = new ListEventListener() {

			@Override
			public void listChanged(ListEvent listChanges) {
				
				System.out.println("Cambio en la lista, tamaño de la lista:\t" + ancolabStackingData.size());
			}
        	
        };
        ancolabStackingData.addListEventListener(listChangeListener);
		
        //$$$$$$$$$$$$$ NATTABLE STACKING
        natTableStacking = StackingNatTableFactory.createTable(parent, this.ancolabStackingData, this.stackingTableSelectionLayer);
        /*
        GridData gridDataStackingTable = new GridData();
        gridDataStackingTable.grabExcessHorizontalSpace = true;
        gridDataStackingTable.grabExcessVerticalSpace = true;
        gridDataStackingTable.horizontalAlignment = SWT.BEGINNING;
        gridDataStackingTable.verticalAlignment = SWT.BEGINNING;
        gridDataStackingTable.minimumHeight = 600;
        gridDataStackingTable.minimumWidth = natTableStacking.getPreferredWidth();

        natTableStacking.setLayoutData(gridDataStackingTable);
        natTableStacking.setBackground(GUIHelper.COLOR_WHITE);
		*/
        
        IConfigRegistry configRegistry = new ConfigRegistry();
        FilterAncolabMaterialGridLayer underlyingLayer = new FilterAncolabMaterialGridLayer(configRegistry);
        //$$$$$$$$$$$$$ NATTABLE MATERIALS
        natTableMaterials = MaterialsNatTableFactory.createTable(parent, configRegistry,   underlyingLayer);
  
        
        int materialTablePreferredWidth = natTableMaterials.getPreferredWidth()+18;
        
        //ESTO ESTA PASADO AL MATERIALS FACTORY
        GridData gridDataMaterialTable = new GridData();
        gridDataMaterialTable.grabExcessHorizontalSpace = true;
        gridDataMaterialTable.grabExcessVerticalSpace = true;
        gridDataMaterialTable.horizontalAlignment = SWT.END;
        gridDataMaterialTable.verticalAlignment = SWT.BEGINNING;
        gridDataMaterialTable.minimumHeight = 300;
        gridDataMaterialTable.horizontalSpan = AncolabConstants.MATERIALS_TABLE_HORIZONTAL_SPAN;
        natTableMaterials.setLayoutData(gridDataMaterialTable);
        
        //ESTO ESTA PASADO AL MATERIALS FACTORY

        parent.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event) {
                GridData data = ((GridData) natTableMaterials.getLayoutData());
                
                int materialsTableActualLayerWidth = parent.getBounds().width * AncolabConstants.MATERIALS_TABLE_HORIZONTAL_SPAN / AncolabConstants.GRIDLAYOUT_NUM_COLUMNS;
                System.out.println("Preferred width\t\t"+ materialTablePreferredWidth);
                System.out.println("Parente Bounds\t\t"+ parent.getBounds().width);
                System.out.println("Table Bounds\t\t"+ materialsTableActualLayerWidth);
                System.out.println("natTableMaterials.getWidth()\t"+ natTableMaterials.getWidth());
                
                // minHeight + 10 because of the margins
                //if (parent.getBounds().width < materialTableMinWidth) {
                if (materialsTableActualLayerWidth < materialTablePreferredWidth) {

                	System.out.println("Resize AAAA");
                	//natTableMaterials.setSize(materialsTableActualLayerWidth, natTableMaterials.getHeight());
                	data.minimumWidth = materialsTableActualLayerWidth;
                } else {
                	System.out.println("Resize BBBB");
                    data.minimumWidth = materialTablePreferredWidth;
                 }
            }
        });    
        
        
        DragAndDropSupport dragSupport =
                new DragAndDropSupport(natTableMaterials, underlyingLayer.getSelectionLayer(), underlyingLayer.getAncolabMaterialData());
        Transfer[] transfer = { TextTransfer.getInstance() };
        natTableMaterials.addDragSupport(DND.DROP_COPY, transfer, dragSupport);
        //natTable.addDropSupport(DND.DROP_COPY, transfer, dndSupport);

        DragAndDropSupport dropSupport =
                new DragAndDropSupport(natTableStacking, this.stackingTableSelectionLayer, this.ancolabStackingData);
        Transfer[] transfer2 = { TextTransfer.getInstance() };
        natTableStacking.addDropSupport(DND.DROP_COPY, transfer2, dropSupport);
        //natTable.addDropSupport(DND.DROP_COPY, transfer, dndSupport);

        
        //Button mirrorButton = new Button(parent, SWT.PUSH);
     //   mirrorButton.setText("Mirror");
     //  mirrorButton.setToolTipText("Mirror current stacking");
        
        //imageLoader.loadImage(this.getClass(), "icons/folder.png");
       // ImageDescriptor mirrorImageDcr = ImageDescriptor.createFromURL(FileLocator.find(bundle, "icons/mirror-stacking.png"));
       // mirrorButton.setImage( imageLoader.loadImage(this.getClass(),"icons/mirror-stacking.png"));
       // mirrorButton.setEnabled(false);
        
        
	}

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
	


}