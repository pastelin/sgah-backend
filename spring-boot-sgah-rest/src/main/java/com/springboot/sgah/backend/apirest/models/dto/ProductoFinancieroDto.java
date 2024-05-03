package com.springboot.sgah.backend.apirest.models.dto;

public class ProductoFinancieroDto {

	private Integer cdApp;
	private String nbApp;

	public ProductoFinancieroDto() {
	}

	public ProductoFinancieroDto(Integer cdApp, String nbApp) {
		this.cdApp = cdApp;
		this.nbApp = nbApp;
	}

	public Integer getCdApp() {
		return cdApp;
	}

	public void setCdApp(Integer cdApp) {
		this.cdApp = cdApp;
	}

	public String getNbApp() {
		return nbApp;
	}

	public void setNbApp(String nbApp) {
		this.nbApp = nbApp;
	}

}
