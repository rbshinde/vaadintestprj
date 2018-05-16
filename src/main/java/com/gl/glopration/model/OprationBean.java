package com.gl.glopration.model;

import java.io.Serializable;

/**
 * A entity object, like in any other Java application. In a typical real world
 * application this could for example be a JPA entity.
 */
@SuppressWarnings("serial")
public class OprationBean implements Serializable, Cloneable {
	private Long id;

	private ProvinceBean provinceBean;

	private String oprationCode = "";

	private String oprationDesciption = "";

	private ReceiptCode receiptCode;

	private Double price = 0.00;

	private Double DLTCharge = 0.00;

	private Double wage = 0.00;

	/**
	 * @return
	 */
	public ProvinceBean getProvinceBean() {
		return provinceBean;
	}

	public void setProvinceBean(ProvinceBean provinceBean) {
		this.provinceBean = provinceBean;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOprationCode() {
		return oprationCode;
	}

	public void setOprationCode(String oprationCode) {
		this.oprationCode = oprationCode;
	}

	public String getOprationDesciption() {
		return oprationDesciption;
	}

	public void setOprationDesciption(String oprationDesciption) {
		this.oprationDesciption = oprationDesciption;
	}

	public ReceiptCode getReceiptCode() {
		return receiptCode;
	}

	public void setReceiptCode(ReceiptCode receiptCode) {
		this.receiptCode = receiptCode;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDLTCharge() {
		return DLTCharge;
	}

	public void setDLTCharge(Double defaultDLTCharge) {
		this.DLTCharge = defaultDLTCharge;
	}

	public Double getWage() {
		return wage;
	}

	public void setWage(Double defaultWage) {
		this.wage = defaultWage;
	}

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.id == null) {
			return false;
		}

		if (obj instanceof OprationBean && obj.getClass().equals(getClass())) {
			return this.id.equals(((OprationBean) obj).id);
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + (id == null ? 0 : id.hashCode());
		return hash;
	}

	@Override
	public OprationBean clone() throws CloneNotSupportedException {
		return (OprationBean) super.clone();
	}

	@Override
	public String toString() {
		return oprationCode + " " + oprationDesciption + " " + receiptCode + " " + price + " " + DLTCharge + " " + wage;
	}
}