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
    
    public static String connectionString = "";
    
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
    
    
    public static boolean retrieveLocalConfiguration() {
    	Document configurationDocument = validateXMLConfiguration();
    	
        if (configurationDocument == null)
            return false;
        
        NodeList replicaSet = configurationDocument.getElementsByTagName("replica");
        String replicaName = configurationDocument.getElementsByTagName("replicaName").item(0).getTextContent();
        
        List<String> replicasAddress = new ArrayList<String>();
        
        for (int i = 0; i < replicaSet.getLength(); i++) {
          Element replica = (Element) replicaSet.item(i);
          String addressDBMS = replica.getElementsByTagName("addressDBMS").item(0).getTextContent();
          String portDBMS = replica.getElementsByTagName("portDBMS").item(0).getTextContent();
          
          replicasAddress.add(addressDBMS + ":" + portDBMS);
        }
         
        connectionString += String.join(",", replicasAddress);
        connectionString += "/?replicaSet=" + replicaName;
        
        // ######## TO BE REMOVED AFTER REPLICA SET IMPLEMENTATION
        connectionString = "localhost:27017";
        // #########
        
        return true;
    }
    
}
