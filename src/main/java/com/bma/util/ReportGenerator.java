package com.bma.util;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


public class ReportGenerator {
	
	public static final String RESOURCE_FOLDER = "resources/jasper/";
	public static final String USER_DATASOURCE = "userDataSource";

	public ReportGenerator() {
		super();
	}
	
	public static void generateReport(final JRDataSource dataSource, final Map<String, Object> map, final String jrxmlFilename, final String targetFilename){
		try {
			final JasperDesign jasperDesign = JRXmlLoader.load(jrxmlFilename);
			final JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			final JasperPrint printReport = JasperFillManager.fillReport(jasperReport, map, dataSource);
			JasperExportManager.exportReportToPdfFile(printReport, targetFilename);

		}	catch (JRException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
