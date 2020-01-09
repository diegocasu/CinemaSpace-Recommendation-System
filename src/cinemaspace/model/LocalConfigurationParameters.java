package cinemaspace.model;


import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.transform.dom.*;


public class LocalConfigurationParameters {
    private static final String XMLConfigurationFilePath = "./configuration.xml";
    private static final String XSDConfigurationFilePath = "./configuration.xsd";
    
    public static String mainArchiveConnectionString = "";
    public static String recommendationArchiveConnectionString = "" ;
	public static String recommendationArchiveUser = "";
	public static String recommendationArchivePassword = "";
    
    private static Document validateXMLConfiguration(){
        Document configurationDocument;
        
        try{
           DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
           SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
           
           configurationDocument = documentBuilder.parse(new File(XMLConfigurationFilePath));
           Schema validationSchema = schemaFactory.newSchema(new File(XSDConfigurationFilePath));
           
           validationSchema.newValidator().validate(new DOMSource(configurationDocument));
           
        } catch(Exception exception) {
           if ( exception instanceof SAXException)
                System.out.println("Validation error: " + exception.getMessage());
           else
                System.out.println(exception.getMessage());
           configurationDocument = null;
        }
        
        return configurationDocument;
    }
    
    private static void setMainArchiveConnectionString(Document configurationDocument) {
    	NodeList mainArchiveNode = configurationDocument.getElementsByTagName("mainArchive");
    	Element mainArchiveParameters = (Element) mainArchiveNode.item(0);
        
    	mainArchiveConnectionString = mainArchiveParameters.getElementsByTagName("addressDBMS").item(0).getTextContent() +
										":" +
										mainArchiveParameters.getElementsByTagName("portDBMS").item(0).getTextContent();	
    }
    
    private static void setRecommendationArchiveConnectionString(Document configurationDocument) {
    	NodeList recommendationArchiveNode = configurationDocument.getElementsByTagName("recommendationArchive");
    	Element recommendationArchiveParameters = (Element) recommendationArchiveNode.item(0);
    	
    	recommendationArchiveUser = recommendationArchiveParameters.getElementsByTagName("user").item(0).getTextContent();
    	recommendationArchivePassword = recommendationArchiveParameters.getElementsByTagName("password").item(0).getTextContent();
        
    	recommendationArchiveConnectionString = recommendationArchiveParameters.getElementsByTagName("addressDBMS").item(0).getTextContent() +
    											":" +
    											recommendationArchiveParameters.getElementsByTagName("portDBMS").item(0).getTextContent();
    }
    
    public static boolean retrieveLocalConfiguration() {
    	Document configurationDocument = validateXMLConfiguration();
    	
        if (configurationDocument == null)
            return false;
        
        setMainArchiveConnectionString(configurationDocument);
        setRecommendationArchiveConnectionString(configurationDocument);
        
        return true;
    }
    
}
