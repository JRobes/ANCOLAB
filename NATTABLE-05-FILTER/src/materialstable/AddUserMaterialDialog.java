package materialstable;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public class AddUserMaterialDialog extends TitleAreaDialog {
	private AncolabMaterial userMaterial;
    private Text txtName;
    private Text thicknessText;

    private Text e1Text;
    private Text e2Text;
    private Text g12Text;
    private Text nu12Text;
    
    private Button compositeButton;
    private Button honeycombButton;
    
    private FocusListener focusListenerHoneycombButton;  
    private FocusListener focusListenerCompositeButton;    

    private SelectionListener selectionListener;
    private ModifyListener modifyListener;

    private String firstName;
    private String lastName;
	public AddUserMaterialDialog(Shell parentShell) {
		super(parentShell);
	}
    @Override
    public void create() {
    	
        super.create();
        
        setTitle("Create new User Material (Composite or Honeycomb)");
        setMessage("User Materials are locally stored", IMessageProvider.INFORMATION);
        
        Button ok = getButton(IDialogConstants.OK_ID);
        if (ok != null)
        	ok.setEnabled(false);
        
        modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				boolean test;
				test = (validate()) ? true : false;
				ok.setEnabled(test);
			}
        };
       
        selectionListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				System.out.println("HONEYCOMB:\t"+honeycombButton.getSelection());
				System.out.println("COMPOSITE:\t"+compositeButton.getSelection());

				boolean test;
				test = (validate()) ? true : false;
				ok.setEnabled(test);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
        };
       
        focusListenerHoneycombButton = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("HONEYCOMB:\t"+honeycombButton.getSelection());
				System.out.println("COMPOSITE:\t"+compositeButton.getSelection());

			}
			@Override
			public void focusLost(FocusEvent e) {
			}
        };
        focusListenerCompositeButton = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("HONEYCOMB:\t"+honeycombButton.getSelection());
				System.out.println("COMPOSITE:\t"+compositeButton.getSelection());
			}
			@Override
			public void focusLost(FocusEvent e) {
			}
        };       
        
        
        //honeycombButton.addFocusListener(focusListenerHoneycombButton);
        //compositeButton.addFocusListener(focusListenerCompositeButton);

        honeycombButton.addSelectionListener(selectionListener);
        compositeButton.addSelectionListener(selectionListener);
        txtName.addModifyListener(modifyListener);
        thicknessText.addModifyListener(modifyListener);
        e1Text.addModifyListener(modifyListener);
        e2Text.addModifyListener(modifyListener);
        g12Text.addModifyListener(modifyListener);
        nu12Text.addModifyListener(modifyListener);
        
    }
    
    public void saveInput() {
    	String type = "Composite";
    	System.out.println("salvado input");
    	if(honeycombButton.getSelection()) 
    		type = "Honeycomb";
    	
    	this.setUserMaterial(new AncolabMaterial(txtName.getText(), "User",type , thicknessText.getText(),
    												e1Text.getText(), e2Text.getText(), g12Text.getText(), nu12Text.getText()));
    }
    
    private boolean validate() {
    	if(honeycombButton.getSelection()) {
    		return validateName() && validateE1() && validateE2() && validateG12() && validateNu12();
    	}
    	return validateName() && validateE1() && validateE2() && validateG12() && validateNu12() && validateThickness();
    
	}
	private boolean validateName() {
		if (txtName.getText().equals(""))
			return false;
		return true;
	}
	private boolean validateE2() {
		if(e2Text.getText().equals("")) {
			return false;
		}
		try {
			Double d = Double.parseDouble(e2Text.getText());
			if(d<=0)
				return false;
			return true;

		}catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean validateE1() {
		if(e1Text.getText().equals("")) {
			return false;
		}
		try {
			Double d = Double.parseDouble(e1Text.getText());
			if(d<=0)
				return false;
			return true;

		}catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}	
	private boolean validateG12() {
		if(g12Text.getText().equals("")) {
			return false;
		}
		try {
			Double d = Double.parseDouble(g12Text.getText());
			if(d<=0)
				return false;
			return true;

		}catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean validateNu12() {
		if(nu12Text.getText().equals("")) {
			return false;
		}
		try {
			Double d = Double.parseDouble(nu12Text.getText());
			if(d<=0)
				return false;
			return true;

		}catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean validateThickness() {
		if(thicknessText.getText().equals("")) {
			return false;
		}
		try {
			Double d = Double.parseDouble(thicknessText.getText());
			if(d<0)
				return false;
			return true;
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean validateTypeAndThickness(){
		System.out.println("CompositeButton\t"+compositeButton.getEnabled());
		System.out.println("Thickness\t"+thicknessText.getText().toString());
		
		if(compositeButton.getSelection()) 
			System.out.println("Dentro del IF\t"+thicknessText.getText().equals(""));

			if(thicknessText.getText().equals("")){
				return false;
			}
		return true;
		

	}

	@Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(3, false);
        container.setLayout(layout);
        
        selectionListener = new SelectionAdapter () {
            public void widgetSelected(SelectionEvent event) {
            	if((event.widget instanceof Button)) {
                    Button button = ((Button) event.widget);
                    if(button.getSelection()&&button.getText().equals("Honeycomb")) {
                		System.out.println("Boton: "+ button.getText()+"\t"+button.getEnabled());
                		thicknessText.setEnabled(false);
                    }
                    else if(button.getSelection()&&button.getText().equals("Composite")) {
                		System.out.println("Boton: "+ button.getText()+"\t"+button.getEnabled());
                		thicknessText.setEnabled(true);


                    }
             	}
 
            };
         };
        createName(container);
        createType(container);
        createThickness(container);
        createE1(container);
        createE2(container);
        createG12(container);
        createNu12(container);
        
        return area;
    }

    private void createNu12(Composite container) {
        Label lbtNu12 = new Label(container, SWT.NONE);
        lbtNu12.setText("Nu12");

        GridData dataNu12 = new GridData();
        dataNu12.horizontalSpan = 2;
        dataNu12.grabExcessHorizontalSpace = true;
        dataNu12.horizontalAlignment = GridData.FILL;

        nu12Text = new Text(container, SWT.BORDER);
        nu12Text.setLayoutData(dataNu12);	
        nu12Text.addSelectionListener(selectionListener);
	}
	private void createG12(Composite container) {
        Label lbtG12 = new Label(container, SWT.NONE);
        lbtG12.setText("G12");

        GridData dataG12 = new GridData();
        dataG12.horizontalSpan = 2;
        dataG12.grabExcessHorizontalSpace = true;
        dataG12.horizontalAlignment = GridData.FILL;

        g12Text = new Text(container, SWT.BORDER);
        g12Text.setLayoutData(dataG12);				
	}
	private void createE2(Composite container) {
        Label lbtE2 = new Label(container, SWT.NONE);
        lbtE2.setText("E2");

        GridData dataE2 = new GridData();
        dataE2.horizontalSpan = 2;
        dataE2.grabExcessHorizontalSpace = true;
        dataE2.horizontalAlignment = GridData.FILL;

        e2Text = new Text(container, SWT.BORDER);
        e2Text.setLayoutData(dataE2);				
	}
	private void createE1(Composite container) {
        Label lbtE1 = new Label(container, SWT.NONE);
        lbtE1.setText("E1");

        GridData dataE1 = new GridData();
        dataE1.horizontalSpan = 2;
        dataE1.grabExcessHorizontalSpace = true;
        dataE1.horizontalAlignment = GridData.FILL;

        e1Text = new Text(container, SWT.BORDER);
        e1Text.setLayoutData(dataE1);		
	}
	private void createThickness(Composite container) {
        Label lbtName = new Label(container, SWT.NONE);
        lbtName.setText("Thickness");

        GridData dataThickness = new GridData();
        dataThickness.horizontalSpan = 2;
        dataThickness.grabExcessHorizontalSpace = true;
        dataThickness.horizontalAlignment = GridData.FILL;

        thicknessText = new Text(container, SWT.BORDER);
        thicknessText.setLayoutData(dataThickness);
	}
	private void createName(Composite container) {
        Label lbtName = new Label(container, SWT.NONE);
        lbtName.setText("Material Name");

        GridData dataName = new GridData();
        dataName.horizontalSpan = 2;
        dataName.grabExcessHorizontalSpace = true;
        dataName.horizontalAlignment = GridData.FILL;

        txtName = new Text(container, SWT.BORDER);
        txtName.setLayoutData(dataName);
    }

    private void createType(Composite container) {
        Label lbtType = new Label(container, SWT.NONE);
        lbtType.setText("Material Type");
        compositeButton = new Button(container, SWT.RADIO);
        compositeButton.setText("Composite");
        compositeButton.addSelectionListener(selectionListener);
        honeycombButton = new Button(container, SWT.RADIO);
        honeycombButton.setText("Honeycomb");
        honeycombButton.addSelectionListener(selectionListener);

    }



    @Override
    protected boolean isResizable() {
        return true;
    }



    @Override
    protected void okPressed() {
        saveInput();
        super.okPressed();
    }
	public AncolabMaterial getUserMaterial() {
		return userMaterial;
	}
	public void setUserMaterial(AncolabMaterial userMaterial) {
		this.userMaterial = userMaterial;
	}
   

 }
