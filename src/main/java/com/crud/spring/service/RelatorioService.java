package com.crud.spring.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public byte[] gerarRelatorio(String nomeRelatorio, ServletContext serveletContext) throws Exception {

		/* Obtem a conexao com o banco de dados */
		Connection connection = jdbcTemplate.getDataSource().getConnection();

	
		
		/* Carrega o caminho do arquivo Jasper */
		String caminhoJasper = serveletContext.getRealPath("relatorios") + File.separator
				+ nomeRelatorio + ".jasper";
		
		
		/* Gerar o relatorio com os dados e conexao */
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, new HashMap(), connection);

		byte[] retorno = JasperExportManager.exportReportToPdf(print);

		connection.close();

		return retorno;
	}
}
