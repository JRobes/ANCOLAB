package aero.alestis.stresstools.ancolab.model;

public final class AncolabConstants {
	
	  public static final int ROW_HEIGHT = 20;
	  public static final int STRING_CELL_WIDTH = 90;
	  public static final int MAT_NAME_CELL_WIDTH = 250;
	  public static final int NUMBER_CELL_WIDTH = 65;
	  
	  public static final int MATERIALS_TABLE_HORIZONTAL_SPAN = 3;
	  public static final int GRIDLAYOUT_NUM_COLUMNS = 4;
	  
	  

	
	  /** System property - <tt>line.separator</tt>*/
	  public static final String NEW_LINE = System.getProperty("line.separator");
	  /** System property - <tt>file.separator</tt>*/
	  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	  /** System property - <tt>path.separator</tt>*/
	  public static final String PATH_SEPARATOR = System.getProperty("path.separator");
	  
	  public static final String EMPTY_STRING = "";
	  public static final String SPACE = " ";
	  public static final String TAB = "\t";
	  public static final String SINGLE_QUOTE = "'";
	  public static final String PERIOD = ".";
	  public static final String DOUBLE_QUOTE = "\"";

	  
	  /**
	   The caller should be prevented from constructing objects of 
	   this class, by declaring this private constructor. 
	  */
	  private AncolabConstants(){
	    //this prevents even the native class from 
	    //calling this ctor as well :
	    throw new AssertionError();
	  }
}
