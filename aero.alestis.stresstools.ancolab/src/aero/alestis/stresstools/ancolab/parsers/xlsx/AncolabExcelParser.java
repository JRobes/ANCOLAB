package aero.alestis.stresstools.ancolab.parsers.xlsx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import aero.alestis.stresstools.ancolab.model.AncolabMaterial;

public class AncolabExcelParser {
	private static String EXCEL_FILE = System.getProperty("user.dir")+"/admisibles-ancolab.xlsx";
	private	FileInputStream file = null;

	private XSSFWorkbook workbook;
	private XSSFSheet worksheet;
	private List<AncolabMaterial> list = new ArrayList<AncolabMaterial>();
	
	public AncolabExcelParser(){
		try {
			File f = new File(EXCEL_FILE);
			System.out.println(f.exists());
			file = new FileInputStream(new File(EXCEL_FILE));
			workbook = new XSSFWorkbook(file);
			worksheet = workbook.getSheet("Sheet1");;

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Iterator<Row> rowIterator = worksheet.iterator();
		rowIterator.next();
		rowIterator.next();
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();	
			
			//System.out.println("MATERIAL: "+getData(row, 1)+"\t"+getData(row, 2)+"\t"+getData(row, 3)+"\t"+getData(row, 4)+"\t"+
			//								 getData(row, 5)+"\t"+getData(row, 6)+"\t"+getData(row, 7)+"\t"+getData(row, 8));
			list.add(new AncolabMaterial(getData(row, 1), getData(row, 2), getData(row, 3),
					                     getData(row, 4), getData(row, 5), getData(row, 6),
					                     getData(row, 7), getData(row, 8)));
			
			
		}
		try {
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(list.size());
	
	}
	  private String getData(Row row,int column) {
	    
	        DataFormatter formatter = new DataFormatter();
	        String data1 = formatter.formatCellValue(row.getCell(column));
	        return data1;
	  }
	  
	  public List<AncolabMaterial> getList(){
		return list;
		  
	  }
}
