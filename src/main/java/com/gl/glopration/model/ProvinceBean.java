package com.gl.glopration.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProvinceBean implements Serializable, Cloneable {

	private Long id;

	private Long operationCode;

	private ProvinceAreas provinceAreas;

	private Double DLTCharge = 0.00;

	private Double wage = 0.00;

	public Long getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(Long operationCode) {
		this.operationCode = operationCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProvinceAreas getProvinceAreas() {
		return provinceAreas;
	}

	public void setProvinceAreas(ProvinceAreas provinceAreas) {
		this.provinceAreas = provinceAreas;
	}

	public Double getDLTCharge() {
		return DLTCharge;
	}

	public void setDLTCharge(Double dLTCharge) {
		DLTCharge = dLTCharge;
	}

	public Double getWage() {
		return wage;
	}

	public void setWage(Double wage) {
		this.wage = wage;
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

		if (obj instanceof ProvinceBean && obj.getClass().equals(getClass())) {
			return this.id.equals(((ProvinceBean) obj).id);
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
	public ProvinceBean clone() throws CloneNotSupportedException {
		return (ProvinceBean) super.clone();
	}

	@Override
	public String toString() {
		return provinceAreas + " " + DLTCharge + " " + wage;
	}
}
