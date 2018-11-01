import aero.alestis.stresstools.ancolab.parsers.xml.AncolabXmlParser;

public class TestXML {
	private static String folderToParse = "C:\\Users\\javier.robes\\Desktop\\TAREAS\\ANCOLAB-MATERIALS";
	public static void main(String[] args) {
		AncolabXmlParser parser = new AncolabXmlParser();
		System.out.println("numero de materiales\t" + parser.parseFolder(folderToParse).size());
		System.out.println("Serializa...\t");
		parser.serialize("jander");

		
		
		
		
	}

}
