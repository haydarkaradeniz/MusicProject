package com.common.util;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
public class XmlHelper {
	
	public static void test() {
		String xml = "C:\\Users\\is96092\\Desktop\\music\\test.xml";
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document dom = db.parse(xml);
	            Element doc = dom.getDocumentElement();
	            String role1 = "";
	            role1 = getTextValue(role1, doc, "note");
	            System.out.println("result : " + role1);
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	}
	
	private static String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    NodeList noteList= doc.getElementsByTagName(tag);
	    if (noteList.getLength() > 0 ) {
	        for(int i=0; i< noteList.getLength(); i++) {
	        	Node note = noteList.item(i);	        	
	        	
	        	Element noteElement = (Element) note;
	        	if (noteElement.getNodeType() == Node.ELEMENT_NODE) {
	        		if(noteElement.getElementsByTagName("pitch").getLength()>0) {
	        			Element pitchElement = (Element) noteElement.getElementsByTagName("pitch").item(0);
	        			System.out.println("Staff idX : " + pitchElement.getElementsByTagName("step").item(0).getTextContent());
	        		}
	        		else {
	        			System.out.println("yoktir");
	        		}					
//					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());	        		
	        	}
	        	else {
	        		System.out.println("not element");
	        	}	        	
	        }
	    	
//	    	value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
	
	public static void testx() {         
	         try {
	        	 File fXmlFile = new File("C:\\Users\\is96092\\Desktop\\music\\Megaman2.xml");
	        		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        		Document doc = dBuilder.parse(fXmlFile);
	        				
	        		//optional, but recommended
	        		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	        		doc.getDocumentElement().normalize();

	        		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	        				
	        		NodeList nList = doc.getElementsByTagName("staff");
	        				
	        		System.out.println("----------------------------");

	        		for (int temp = 0; temp < nList.getLength(); temp++) {

	        			Node nNode = nList.item(temp);
	        					
	        			System.out.println("\nCurrent Element :" + nNode.getNodeName());
	        					
	        			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	        				Element eElement = (Element) nNode;

	        				System.out.println("Staff id : " + eElement.getAttribute("id"));
	        				System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
	        				System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
	        				System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
	        				System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

	        			}
	        		}
	        	    } catch (Exception e) {
	        		e.printStackTrace();
	        	    }
	   }
}
