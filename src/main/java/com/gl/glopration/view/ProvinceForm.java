package com.gl.glopration.view;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.web.client.RestTemplate;

import com.gl.glopration.model.OprationBean;
import com.gl.glopration.model.ProvinceAreas;
import com.gl.glopration.model.ProvinceBean;
import com.gl.glopration.model.ReceiptCode;
import com.gl.glopration.service.ProvinceService;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class ProvinceForm extends FormLayout {

	public static final String REST_SERVICE_URI = "http://localhost:8080/GLOprations/provinces";

	private Label opration = new Label("Opration by Area");
	private NativeSelect<ProvinceAreas> provinceAreas = new NativeSelect<>("Province Area");
	private TextField DLTCharge = new TextField("DLT Charge");
	private TextField wage = new TextField("Default Wage");
	private Grid<ProvinceBean> grid = new Grid<>(ProvinceBean.class);

	// private ProvinceService provinceService = ProvinceService.getInstance();

	private Button save = new Button(VaadinIcons.PLUS_CIRCLE_O);
	private Button delete = new Button(VaadinIcons.MINUS_CIRCLE_O);

	private ProvinceBean provinceBeanBean;
	private MyUI myUI;
	private Binder<ProvinceBean> binder = new Binder<>(ProvinceBean.class);

	public ProvinceForm(MyUI myUI) {
		this.myUI = myUI;

		provinceAreas.setRequiredIndicatorVisible(true);
		DLTCharge.setRequiredIndicatorVisible(true);
		wage.setRequiredIndicatorVisible(true);

		grid.setColumns("provinceAreas", "DLTCharge", "wage");

		GridLayout gridCssLayout = new GridLayout(1, 1);
		gridCssLayout.addComponents(grid);
		gridCssLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		grid.setHeightByRows(3);

		setSizeUndefined();
		HorizontalLayout header = new HorizontalLayout(opration);
		// HorizontalLayout gridHorzontalLayout = new HorizontalLayout(gridCssLayout);
		HorizontalLayout buttons = new HorizontalLayout(save, delete);
		addComponents(header, provinceAreas, DLTCharge, wage, buttons, gridCssLayout);

		gridCssLayout.setSizeFull();
		header.setComponentAlignment(opration, Alignment.MIDDLE_LEFT);
		header.setStyleName(ValoTheme.LABEL_COLORED);
		header.setSizeFull();
		provinceAreas.setItems(ProvinceAreas.values());
		save.setClickShortcut(KeyCode.ENTER);

		updateProvince();

		setProvince(new ProvinceBean());
		// binder.bindInstanceFields(this);

		bindAllFieldsManually();

		save.addClickListener(e -> this.save());
		delete.addClickListener(e -> this.delete());
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				// form.setVisible(false);
			} else {
				setProvince(event.getValue());
			}
		});
	}

	private void bindAllFieldsManually() {
		binder.forField(provinceAreas).asRequired("mandatorry filed ").bind(ProvinceBean::getProvinceAreas,
				ProvinceBean::setProvinceAreas);

		binder.forField(DLTCharge).withConverter(Double::valueOf, String::valueOf,
				// Text to use instead of the NumberFormatException message
				"Please enter a number").bind(ProvinceBean::getDLTCharge, ProvinceBean::setDLTCharge);
		binder.forField(wage).withConverter(Double::valueOf, String::valueOf,
				// Text to use instead of the NumberFormatException message
				"Please enter a number").bind(ProvinceBean::getWage, ProvinceBean::setWage);

	}

	public void setProvince(ProvinceBean bean) {
		this.provinceBeanBean = bean;
		binder.setBean(provinceBeanBean);

		setVisible(true);
		// Show delete button for only customers already in the database
		DLTCharge.selectAll();
	}

	private void delete() {
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("Province id in delelete : " + provinceBeanBean.getId());
		restTemplate.delete(REST_SERVICE_URI + "/deleteProvince/" + provinceBeanBean.getId());
		updateProvince();

	}

	private void save() {
		System.out.println("Province id in save : " + provinceBeanBean.getId());
		if (!Optional.ofNullable(provinceBeanBean.getProvinceAreas()).isPresent()) {
			return;
		}
		System.out.println("Province id in save : " + provinceBeanBean.getId());
		RestTemplate restTemplate = new RestTemplate();
		URI uri = restTemplate.postForLocation(REST_SERVICE_URI + "/createProvince/", provinceBeanBean,
				ProvinceBean.class);
		grid.asSingleSelect().clear();
		setProvince(new ProvinceBean());
		updateProvince();
	}

	public void updateProvince() {
		System.out.println("in update provindddDDDDDDDDDDDDD");
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI + "/getProvinces/",
				List.class);
		List<ProvinceBean> provinceBeanBeanList = new ArrayList<ProvinceBean>();
		if (usersMap != null) {
			for (LinkedHashMap<String, Object> map : usersMap) {
				ProvinceBean provinceBean = new ProvinceBean();

				int intVal = (int) map.get("id");
				provinceBean.setId((long) intVal);

				if (ProvinceAreas.BANGKOK.toString().equals(map.get("provinceAreas") + ""))
					provinceBean.setProvinceAreas(ProvinceAreas.BANGKOK);
				else if (ProvinceAreas.BUENG_KAN.toString().equals(map.get("provinceAreas") + ""))
					provinceBean.setProvinceAreas(ProvinceAreas.BUENG_KAN);
				else if (ProvinceAreas.BURIRAM.toString().equals(map.get("provinceAreas") + ""))
					provinceBean.setProvinceAreas(ProvinceAreas.BURIRAM);
				else if (ProvinceAreas.CHACHOENGSAO.toString().equals(map.get("provinceAreas") + ""))
					provinceBean.setProvinceAreas(ProvinceAreas.CHACHOENGSAO);

				provinceBean.setWage((Double) map.get("wage"));
				provinceBean.setDLTCharge((Double) map.get("dltcharge"));

				provinceBeanBeanList.add(provinceBean);

			}
		} else {
			System.out.println("No user exist----------");
		}
		grid.setItems(provinceBeanBeanList);
	}

	public ProvinceBean getProvinceBeanBean() {
		return provinceBeanBean;
	}

	public void setProvinceBeanBean(ProvinceBean provinceBeanBean) {
		this.provinceBeanBean = provinceBeanBean;
	}
}
