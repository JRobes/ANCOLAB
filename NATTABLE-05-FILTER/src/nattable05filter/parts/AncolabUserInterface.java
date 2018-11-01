package nattable05filter.parts;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.services.internal.events.EventBroker;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.e4.selection.E4SelectionListener;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;

import aero.alestis.stresstools.ancolab.business.FacadeBusinessAncolabMaterials;
import aero.alestis.stresstools.ancolab.db.FacadeDbAncolabMaterials.LoadSaveStrategy;
import aero.alestis.stresstools.ancolab.model.AncolabMaterial;
import aero.alestis.stresstools.ancolab.model.AncolabStackingLayer;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import nattable05filter.adddeleterows.AddRowCommand;

public class AncolabUserInterface {
    
	final EventList<AncolabStackingLayer> ancolabStackingData  = GlazedLists.eventListOf();
    EventList<AncolabMaterial> ancolabMaterialsData = GlazedLists.eventListOf();
	
    List<AncolabMaterial> theList = new ArrayList<AncolabMaterial>();
    
    
    
    private IConfigRegistry configRegistryMaterials;
	
	private FilterAncolabMaterialGridLayer filterAncolabMaterialGridLayer;

	private SelectionLayer selectionLayerMaterialTable;
    private SelectionLayer stackingTableSelectionLayer;
	
    private  NatTable natTableStacking;
	@SuppressWarnings("unused")
	private  NatTable natTableMaterials;

	private FormToolkit toolkit;
	private ScrolledForm form;
	private Button mirrorStackingButton;
	private Button duplicateStackingButton;
	private Button addUserMaterialButton;
	private Button removeUserMaterialButton;
	private Label  trash;
	private Label  trash2;
	private Action exportButtonAction;
	private Action runButtonAction;
	private Shell  shell;
	
	@SuppressWarnings("restriction")
	@Inject EventBroker broker;
    @Inject ESelectionService service;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		 shell = parent.getShell();
		 toolkit = new FormToolkit(parent.getDisplay());
		 form = toolkit.createScrolledForm(parent);
		 toolkit.decorateFormHeading(form.getForm());	
		 IToolBarManager toolBarManager =form.getToolBarManager();
		 Bundle bundle = FrameworkUtil.getBundle(this.getClass());
		 URL url = FileLocator.find(bundle, new Path("icons/lrun_obj.png"), null);
		 ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(url);
		 runButtonAction = new Action("", imageDescriptor) {
			    @Override
			    public void run() {
			        // Perform action
			    }
		 };
		 url = FileLocator.find(bundle, new Path("icons/export_wiz.gif"), null);
		 imageDescriptor = ImageDescriptor.createFromURL(url);
		 exportButtonAction = new Action("", imageDescriptor) {
			    @Override
			    public void run() {
			        // Perform action
			    }
		 }; 
		 exportButtonAction.setToolTipText("Export material (Nastran card format) to file");
		 runButtonAction.setToolTipText("Execute");
		 exportButtonAction.setEnabled(false);
		 runButtonAction.setEnabled(false);
		 toolBarManager.add(exportButtonAction);
		 toolBarManager.add(runButtonAction);
		 
		 form.getToolBarManager().update(true);
		 //form.setText("ANCOLAB");
		 
		 TableWrapLayout principalLayout = new TableWrapLayout();
		 principalLayout.topMargin=10;
		 principalLayout.bottomMargin=10;
		 principalLayout.numColumns = 2;
		 principalLayout.makeColumnsEqualWidth = false;
		 form.getBody().setLayout(principalLayout);

		 //BEGGINING OF STACKING TABLE SECTION
		 Section sectionStakingTable = toolkit.createSection(form.getBody(), Section.DESCRIPTION|Section.TITLE_BAR| Section.TWISTIE|Section.EXPANDED);
		 TableWrapData sectionStakingTabledata = new TableWrapData(TableWrapData.FILL_GRAB);
		 sectionStakingTabledata.rowspan = 4;
		 sectionStakingTable.setLayoutData(sectionStakingTabledata);
		 sectionStakingTable.setText("Stacking Table");
		 sectionStakingTable.setDescription("Drop materials from Material Table to set composite laminate (thickness or orientation\n");
		 Composite compositeOfSectionStackingTable = toolkit.createComposite(sectionStakingTable);
		 GridLayout gridLayoutForSectionStackingTable = new GridLayout(4,false);
		 compositeOfSectionStackingTable.setLayout(gridLayoutForSectionStackingTable);

		 natTableStacking = StackingNatTableFactory.createTable(compositeOfSectionStackingTable, this.ancolabStackingData, this.stackingTableSelectionLayer);
	     GridData gridDataStackingTable = new GridData();
	     gridDataStackingTable.horizontalSpan = 4;
		 gridDataStackingTable.grabExcessHorizontalSpace = true;
	     gridDataStackingTable.grabExcessVerticalSpace = true;
	     gridDataStackingTable.horizontalAlignment = SWT.CENTER;
	     gridDataStackingTable.verticalAlignment   = SWT.BEGINNING;
	     gridDataStackingTable.minimumHeight = 600;
	     gridDataStackingTable.minimumWidth = natTableStacking.getPreferredWidth();

	     natTableStacking.setLayoutData(gridDataStackingTable);
		 
		 mirrorStackingButton = new Button(compositeOfSectionStackingTable,  SWT.PUSH | SWT.COLOR_GRAY);	 
		 mirrorStackingButton.setEnabled(false);
		 mirrorStackingButton.setToolTipText("Mirror Stack");
		 URL urlMirrorStackingButton = FileLocator.find(bundle, new Path("icons/mirror-stacking.png"), null);
		 ImageDescriptor imageDescriptorMirrorStackingButton = ImageDescriptor.createFromURL(urlMirrorStackingButton);
		 mirrorStackingButton.setImage(imageDescriptorMirrorStackingButton.createImage());		 
		 mirrorStackingButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseUp(MouseEvent e) {
				EventList<AncolabStackingLayer> aux = GlazedLists.eventListOf();
				ListIterator<AncolabStackingLayer> it = ancolabStackingData.listIterator(ancolabStackingData.size());
			    while(it.hasPrevious()) {
			    	AncolabStackingLayer item = it.previous();
			      aux.add((AncolabStackingLayer) item.clone());
			    }
				ancolabStackingData.addAll(aux);
				natTableStacking.refresh();
			}
		 });
		 
		 GridData gridDataMirrorStackingButton = new GridData();
		 gridDataMirrorStackingButton.horizontalSpan=1;
		 gridDataMirrorStackingButton.horizontalAlignment = SWT.END;
		 //gridDataMirrorStackingButton.verticalAlignment   = SWT.BOTTOM;
		 gridDataMirrorStackingButton.grabExcessHorizontalSpace = true;
		 gridDataMirrorStackingButton.grabExcessVerticalSpace = true;
		 gridDataMirrorStackingButton.widthHint = 100;
		 
		 //FormData formData2 = new FormData(40,40);
		 //formData2.top = new FormAttachment(natTableStacking,5);
		 //formData2.bottom = new FormAttachment(100,-5);
		 //formData2.left = new FormAttachment(20,0);
		// formData2.right = new FormAttachment(50,-5);
		 
		 mirrorStackingButton.setLayoutData(gridDataMirrorStackingButton);
 
		 //Button duplicateStackingButton = toolkit.createButton(compositeOfSectionStackingTable,  "", SWT.PUSH | SWT.COLOR_GRAY);	 
		 duplicateStackingButton = new Button(compositeOfSectionStackingTable,  SWT.PUSH | SWT.COLOR_GRAY);
		 duplicateStackingButton.setEnabled(false);
		 duplicateStackingButton.setToolTipText("Duplicate Stack");
		 URL urlDuplicateStackingButton = FileLocator.find(bundle, new Path("icons/duplicate-stacking.png"), null);
		 //URL urlDuplicateStackingButton = FileLocator.find(bundle, new Path("icons/papelera.png"), null);

		 ImageDescriptor imageDescriptorDuplicateStackingButton = ImageDescriptor.createFromURL(urlDuplicateStackingButton);
		 duplicateStackingButton.setImage(imageDescriptorDuplicateStackingButton.createImage());		 
		 duplicateStackingButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseUp(MouseEvent e) {
				EventList<AncolabStackingLayer> aux = GlazedLists.eventListOf();
				for(AncolabStackingLayer toDuplicate : ancolabStackingData) {
					AncolabStackingLayer ancAux = new AncolabStackingLayer();
					ancAux = (AncolabStackingLayer) toDuplicate.clone();
					aux.add(ancAux);
				}
				//aux.addAll(ancolabStackingData);
				ancolabStackingData.addAll(aux);
				natTableStacking.refresh();
			}
		 });

		 GridData griDataDuplicateStackingButton = new GridData();
		 griDataDuplicateStackingButton.horizontalSpan=1;
		 griDataDuplicateStackingButton.horizontalAlignment = SWT.BEGINNING;
		 griDataDuplicateStackingButton.grabExcessHorizontalSpace = true;
		// FormData formData3 = new FormData(40,40);
		 //formData3.top = new FormAttachment(natTableStacking,5);
		 //formData3.bottom = new FormAttachment(100,-5);
		 //formData3.left = new FormAttachment(50,5);
		 //formData3.right = new FormAttachment(50,-5);		 
		 duplicateStackingButton.setLayoutData(griDataDuplicateStackingButton);
		 
		 
		 trash = new Label(compositeOfSectionStackingTable, SWT.BORDER);
		 URL urlTrash = FileLocator.find(bundle, new Path("icons/papelera.png"), null);
		 ImageDescriptor imageDescriptorTrash = ImageDescriptor.createFromURL(urlTrash);

		 trash.setImage(imageDescriptorTrash.createImage());

		 GridData griDataTrash = new GridData();
		 griDataTrash.horizontalSpan=1;
		 griDataTrash.horizontalAlignment = SWT.BEGINNING;
		 griDataTrash.grabExcessHorizontalSpace = true;
		 trash.setLayoutData(griDataTrash);

		 
		 
		 trash2 = new Label(compositeOfSectionStackingTable, SWT.BORDER);
		 trash2.setImage(imageDescriptorTrash.createImage());

		 GridData griDataTrash2 = new GridData();
		 griDataTrash2.horizontalSpan=1;
		 griDataTrash2.horizontalAlignment = SWT.BEGINNING;
		 griDataTrash2.grabExcessHorizontalSpace = true;
		 trash2.setLayoutData(griDataTrash2);
		 
		 sectionStakingTable.setClient(compositeOfSectionStackingTable);
		 //END OF STACKING TABLE SECTION
		 
		 
		 //BEGGINING OF MATERIAL TABLE SECTION
		 Section sectionMaterialsTable = toolkit.createSection(form.getBody(), Section.DESCRIPTION|Section.TITLE_BAR| Section.TWISTIE|Section.EXPANDED);
		 TableWrapData sectionMaterialsTabledata = new TableWrapData(TableWrapData.FILL_GRAB);
		 sectionMaterialsTabledata.colspan = 2;
		 sectionMaterialsTable.setLayoutData(sectionMaterialsTabledata);
		 sectionMaterialsTable.setText("Materials Table");
		 sectionMaterialsTable.setDescription("Drag the selected materials\n");
		 Composite compositeOfSectionMaterialsTable = toolkit.createComposite(sectionMaterialsTable);		 
		 GridLayout gridLayoutForMaterialsTable = new GridLayout(2,false);
		 compositeOfSectionMaterialsTable.setLayout(gridLayoutForMaterialsTable);
		 
		 configRegistryMaterials =new ConfigRegistry();
		 filterAncolabMaterialGridLayer = new FilterAncolabMaterialGridLayer(configRegistryMaterials);
		 
		 
		 theList = FacadeBusinessAncolabMaterials.INSTANCE.getAllMaterials(LoadSaveStrategy.SERIALIZABLE, 
	        		"C:\\Users\\javier.robes\\Desktop\\TAREAS\\ANCOLAB-MATERIALS\\serial.ser");
		 
		 this.ancolabMaterialsData = GlazedLists.eventList(theList);
		 natTableMaterials = FactoryMaterialsTable.createTable(compositeOfSectionMaterialsTable, ancolabMaterialsData);
		// natTableMaterials = MaterialsNatTableFactory.createTable(compositeOfSectionMaterialsTable, configRegistryMaterials,   filterAncolabMaterialGridLayer);
	        
	     GridData gridDataMaterialTable = new GridData();
	     gridDataMaterialTable.horizontalSpan = 2;

	     gridDataMaterialTable.grabExcessHorizontalSpace = true;
	     gridDataMaterialTable.grabExcessVerticalSpace = true;
	     gridDataMaterialTable.horizontalAlignment = SWT.CENTER;
	     gridDataMaterialTable.verticalAlignment = SWT.BEGINNING;
	     gridDataMaterialTable.minimumHeight = 200;
	     gridDataMaterialTable.minimumWidth = natTableMaterials.getPreferredWidth();
	    //Menu menu = new Menu(natTableMaterials);
	     natTableMaterials.setLayoutData(gridDataMaterialTable);
	     


	     
	    // UiBindingRegistry uiBindingRegistry = natTableMaterials.getUiBindingRegistry();
	    // uiBindingRegistry.registerMouseDownBinding(new MouseEventMatcher(SWT.NONE, null, MouseEventMatcher.RIGHT_BUTTON), new PopupMenuAction(menu));
	     removeUserMaterialButton = new Button(compositeOfSectionMaterialsTable,  SWT.PUSH | SWT.COLOR_GRAY);
	     removeUserMaterialButton.setEnabled(true);
	     removeUserMaterialButton.setToolTipText("Remove selected user Material");
		 URL urlRemoveUserMaterialButton = FileLocator.find(bundle, new Path("icons/remove_snippet.png"), null);
		 //URL urlDuplicateStackingButton = FileLocator.find(bundle, new Path("icons/papelera.png"), null);

		 ImageDescriptor imageDescriptorRemoveUserMaterialButton = ImageDescriptor.createFromURL(urlRemoveUserMaterialButton);
		 removeUserMaterialButton.setImage(imageDescriptorRemoveUserMaterialButton.createImage());		 
		 removeUserMaterialButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@Override
			public void mouseUp(MouseEvent e) {
				//ancolabMaterialsData
				/*
				EventList<AncolabMaterial> aux = GlazedLists.eventListOf();
				for(AncolabMaterial toDuplicate : ancolabMaterialsData) {
					AncolabMaterial ancAux = new AncolabMaterial();
					ancAux = (AncolabMaterial) toDuplicate.clone();
					aux.add(ancAux);
				}
				//aux.addAll(ancolabStackingData);
				ancolabStackingData.addAll(aux);
				natTableStacking.refresh();
				*/
			}
		 });

		 GridData griDataRemoveUserMaterialButton = new GridData();
		 griDataRemoveUserMaterialButton.horizontalSpan=1;
		 griDataRemoveUserMaterialButton.horizontalAlignment = SWT.BEGINNING;
		 griDataRemoveUserMaterialButton.grabExcessHorizontalSpace = true;
		// FormData formData3 = new FormData(40,40);
		 //formData3.top = new FormAttachment(natTableStacking,5);
		 //formData3.bottom = new FormAttachment(100,-5);
		 //formData3.left = new FormAttachment(50,5);
		 //formData3.right = new FormAttachment(50,-5);		 
		 removeUserMaterialButton.setLayoutData(griDataRemoveUserMaterialButton);     
	     
	     addUserMaterialButton = new Button(compositeOfSectionMaterialsTable,  SWT.PUSH | SWT.COLOR_GRAY);
	     addUserMaterialButton.setEnabled(true);
	     addUserMaterialButton.setToolTipText("Remove selected user Material");
		 URL urlAddUserMaterialButton = FileLocator.find(bundle, new Path("icons/add_snippet.png"), null);
		 //URL urlDuplicateStackingButton = FileLocator.find(bundle, new Path("icons/papelera.png"), null);

		 ImageDescriptor imageDescriptorAddUserMaterialButton = ImageDescriptor.createFromURL(urlAddUserMaterialButton);
		 addUserMaterialButton.setImage(imageDescriptorAddUserMaterialButton.createImage());		 
		 addUserMaterialButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			@Override
			public void mouseDown(MouseEvent e) {
			}
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void mouseUp(MouseEvent e) {
				
				AddUserMaterialDialog dialog = new AddUserMaterialDialog(shell);
				dialog.create();
				if (dialog.open() == Window.OK) {
					AncolabMaterial matToAdd = dialog.getUserMaterial();
					System.out.println("NEW MATERIAL TO BE ADDED TO THE LIST " +matToAdd.getName());
					//System.out.println("ANTES DE ACTUALIZAR LA tabla de materiales...@@@@@@@@@2������������5");
					//System.out.println("Tama�o antiguo" +ancolabMaterialsData.size());
					
					natTableMaterials.doCommand(new AddRowCommand(natTableMaterials, matToAdd));
					//ancolabMaterialsData.add(matToAdd);
					//System.out.println("Antes de a�adir material "+filterAncolabMaterialGridLayer.getAncolabMaterialData().size());
					//filterAncolabMaterialGridLayer.getAncolabMaterialData().add(matToAdd);
					//System.out.println("Despues de a�adir material "+filterAncolabMaterialGridLayer.getAncolabMaterialData().size());
					//natTableMaterials.update();
					//natTableMaterials.refresh();
					
            		//System.out.println("Aqui deberia haber actualizado la tabla de materiales...@@@@@@@@@2������������5");
					//System.out.println("Nuevo tama�o" + ancolabMaterialsData.size());

				}
				
				
			}
		 });

		 GridData griDataAddUserMaterialButton = new GridData();
		 griDataAddUserMaterialButton.horizontalSpan=1;
		 griDataAddUserMaterialButton.horizontalAlignment = SWT.BEGINNING;
		 griDataAddUserMaterialButton.grabExcessHorizontalSpace = true;
		// FormData formData3 = new FormData(40,40);
		 //formData3.top = new FormAttachment(natTableStacking,5);
		 //formData3.bottom = new FormAttachment(100,-5);
		 //formData3.left = new FormAttachment(50,5);
		 //formData3.right = new FormAttachment(50,-5);		 
		 addUserMaterialButton.setLayoutData(griDataAddUserMaterialButton);     		 
	   
	/*	 
		 
	        //CREATE SELECTION LISTENENER FOR MATERIALS TABLE TO ENABLE DELETE BUTTON FOR USER MATERIAL
	        // create a E4SelectionListener and configure it for providing selection
	        // on cell selection
	        @SuppressWarnings({ "rawtypes", "unchecked" })
			E4SelectionListener esl = new E4SelectionListener<>(service,
	        													filterAncolabMaterialGridLayer.getSelectionLayer(),
	        													(IRowDataProvider)filterAncolabMaterialGridLayer.getDataProvider());
	        esl.setFullySelectedRowsOnly(false);
	        esl.setHandleSameRowSelection(false);
	        selectionLayerMaterialTable.addLayerListener(esl);	     
	     
	 */
		 
		 sectionMaterialsTable.setClient(compositeOfSectionMaterialsTable);
		 //END OF MATERIAL TABLE SECTION
		 
		 
		 //BEGGINING OF RESULTS TABLE SECTION
		 Section sectionResultsTable = toolkit.createSection(form.getBody(), Section.DESCRIPTION|Section.TITLE_BAR| Section.TWISTIE|Section.EXPANDED);
		 TableWrapData sectionResultsTabledata = new TableWrapData(TableWrapData.FILL_GRAB);
		 sectionMaterialsTabledata.colspan = 1;
		 sectionResultsTable.setLayoutData(sectionResultsTabledata);
		 sectionResultsTable.setText("Results Table");
		 sectionResultsTable.setDescription("TODO \n");
		 Composite compositeOfSectionResultsTable = toolkit.createComposite(sectionResultsTable);		 
	

		 sectionResultsTable.setClient(compositeOfSectionResultsTable);
		 //END OF RESULTS TABLE SECTION
	 			
	     @SuppressWarnings("rawtypes")
		 ListEventListener listChangeListener = new ListEventListener() {
	    	 	@SuppressWarnings("restriction")
				@Override
				public void listChanged(ListEvent listChanges) {
					
					System.out.println("Cambio en la lista, tama�o de la lista:\t" + ancolabStackingData.size());
					if(ancolabStackingData.isEmpty()) broker.post(AncolabEventConstants.STACKING_LIST_EMPTY, "");
						broker.post(AncolabEventConstants.STACKING_LIST_NOT_EMPTY, "");
				}
	        };
	     ancolabStackingData.addListEventListener(listChangeListener);
	        
	}
	
	
	@Inject
	@Optional
	public void updateButtonStatus(@UIEventTopic(AncolabEventConstants.STACKING_LIST_ALL_EVENTS) Event event) {
		String topic = event.getTopic();

		switch(topic){
    		case AncolabEventConstants.STACKING_LIST_EMPTY:
    			mirrorStackingButton.setEnabled(false);
    			duplicateStackingButton.setEnabled(false);
    			runButtonAction.setEnabled(false);
    			exportButtonAction.setEnabled(false);
    			break;
    		case AncolabEventConstants.STACKING_LIST_NOT_EMPTY:
    			mirrorStackingButton.setEnabled(true);
    			duplicateStackingButton.setEnabled(true);
    			exportButtonAction.setEnabled(true);
    			runButtonAction.setEnabled(true);
    			break;
    		
		}	
	}
	
    @Inject
    @Optional
    void handleMaterialSelection(@Named(IServiceConstants.ACTIVE_SELECTION) List selected) {
        System.out.println("ENTRA EN EL SELECTED @@@@@@@@@@@@@@@@@@@@@@@@@@2");

    	
    	if (selected != null) {
        	AncolabMaterial sel = (AncolabMaterial)selected.get(0);
            System.out.println("MATERIAL SELECTED:\t"+sel.getName());
        	
        }
    }
	
	@PersistState
	public void persistState(MPart part) {

	}
	
	
	@Focus
	public void setFocus() {
		//form.setFocus();
	}
	@PreDestroy
	public void dispose() {

		 
	}
	
	
}