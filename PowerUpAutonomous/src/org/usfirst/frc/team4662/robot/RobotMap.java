/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4662.robot;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * xml input provides value.
 */
public class RobotMap {
	private String m_iniFilename;
	private Document m_IniDoc;
	private XPath m_xPath;
	private boolean m_isDashboardTest; 
	
	public RobotMap(String filename) {
		m_iniFilename = filename;
		try {
			File iniFile = new File(m_iniFilename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();
			
			m_IniDoc = dBuilder.parse(iniFile);
			m_IniDoc.getDocumentElement().normalize();
			m_xPath = XPathFactory.newInstance().newXPath();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		m_isDashboardTest = getBooleanValue("dashboardtest"); 
		 
	}
	
	public boolean isDashboardTest() {
		return m_isDashboardTest;
	}
	
	public void toggleDashboardTest() {
		m_isDashboardTest = !m_isDashboardTest;
	}
	
	private String getNodeVal(String argNode) {
		String elementVal = "";
		try {
			String searchExpr = "/robot/" + argNode + "";
			NodeList nodeList = (NodeList) m_xPath.compile(searchExpr).evaluate(m_IniDoc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					try {
						elementVal = eElement.getTextContent();
					} catch (Exception e) {
						
					}
				}
			} 
				
		} catch(Exception e) {
			
		}
		return elementVal;
	}
	private boolean getBooleanValue(String node) {
		boolean retBool = false;
		if ( getNodeVal(node).equalsIgnoreCase("true")) {
			retBool = true;
			
		}
		return retBool;
 
	}
	private String getNodeChildSVal(String argNode, String nodeName, String nodeValue, String tagName){
		String nodeChildVal = "";
		try{
			String searchExpr = "/robot/" + argNode + "[@" + nodeName + "='" +nodeValue + "']/" + tagName + "/text()";
			NodeList nodeList = (NodeList) m_xPath.compile(searchExpr).evaluate(m_IniDoc, XPathConstants.NODESET);
			for (int i= 0; i < nodeList.getLength(); i++) {
				nodeChildVal = nodeList.item(i).getNodeValue();
			}
		} catch (XPathExpressionException e){
		}
		
		return nodeChildVal;
	}
	
	public double getPIDPVal(String nodeValue){
		return getPIDPVal(nodeValue, 0.2);
	}
	public double getPIDPVal(String nodeValue, double pVal){
		return 	getDoubleValue("pid", "pidcontroller", nodeValue, "p", pVal);
		
	}

	public double getPIDIVal(String nodeValue){
		return getPIDIVal(nodeValue, 0.0);
	}
	public double getPIDIVal(String nodeValue, double iVal){
		return 	getDoubleValue("pid", "pidcontroller", nodeValue, "i", iVal);
		
	}
	public double getPIDDVal(String nodeValue){
		return getPIDDVal(nodeValue, 0.3);
	}
	public double getPIDDVal(String nodeValue, double dVal){
		return 	getDoubleValue("pid", "pidcontroller", nodeValue, "d", dVal);
		
	}
	public double getPIDFVal(String nodeValue){
		return getPIDFVal(nodeValue, 0.2);
	}
	public double getPIDFVal(String nodeValue, double fVal){
		return 	getDoubleValue("pid", "pidcontroller", nodeValue, "f", fVal);
		
	}
	public double getPIDToleranceVal(String nodeValue){
		return getPIDToleranceVal(nodeValue, 0.1);
	}
	public double getPIDToleranceVal(String nodeValue, double tVal){
		return 	getDoubleValue("pid", "pidcontroller", nodeValue, "tolerance", tVal);
		
	}
	
	public int getPortNumber(String nodeValue){
		return getIntValue("device", "name", nodeValue, "port", -1);
	}
	
	private double getDoubleValue(String argNode, String nodeName, String nodeValue, String tagName, double dDefault) {
		double dVal = dDefault;
		try {
			dVal = Double.valueOf( getNodeChildSVal(argNode, nodeName, nodeValue, tagName));
		} catch(Exception e){
			
		}
		return dVal;
	}
	
	private int getIntValue(String argNode, String nodeName, String nodeValue, String tagName, int iDefault) {
		int iVal = iDefault;
		try {
			iVal = Integer.valueOf( getNodeChildSVal(argNode, nodeName, nodeValue, tagName));
		} catch(Exception e){
			
		}
		return iVal;
	}
}
