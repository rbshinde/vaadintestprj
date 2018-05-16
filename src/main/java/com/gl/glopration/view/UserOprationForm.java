package com.gl.glopration.view;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import com.gl.glopration.model.OprationBean;
import com.gl.glopration.model.ProvinceBean;
import com.gl.glopration.model.ReceiptCode;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.web.client.RestTemplate;

public class UserOprationForm extends FormLayout {
	public static final String REST_SERVICE_URI = "http://localhost:8080/GLOprations/oprations";
	private Label opration = new Label("Opration");
	private TextField oprationCode = new TextField("Opration Code");
	private TextField oprationDesciption = new TextField("Opration Desciption");
	private NativeSelect<ReceiptCode> receiptCode = new NativeSelect<>("Receipt Code");
	private TextField price = new TextField("Price");
	private TextField defaultDLTCharge = new TextField("Default DLT Charge");
	private TextField defaultWage = new TextField("Default Wage");

	// oprationCode.setRequiredError("First name must be filled in!");

	private Button save = new Button(VaadinIcons.PLUS_CIRCLE_O);
	private Button delete = new Button(VaadinIcons.MINUS_CIRCLE_O);
	private Button refresh = new Button(VaadinIcons.REFRESH);

	/*
	 * @Autowired OprationService oprationService;
	 */

	private OprationBean oprationBean;
	private MyUI myUI;
	private Binder<OprationBean> binder = new Binder<>(OprationBean.class);

	public UserOprationForm(MyUI myUI) {
		this.myUI = myUI;

		oprationCode.setRequiredIndicatorVisible(true);
		oprationDesciption.setRequiredIndicatorVisible(true);
		receiptCode.setRequiredIndicatorVisible(true);
		price.setRequiredIndicatorVisible(true);
		defaultDLTCharge.setRequiredIndicatorVisible(true);
		defaultWage.setRequiredIndicatorVisible(true);
		// oprationCode.setRequired(true);

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, delete, refresh);
		HorizontalLayout header = new HorizontalLayout(opration);
		addComponents(header, oprationCode, oprationDesciption, receiptCode, price, defaultDLTCharge, defaultWage,
				buttons);

		header.setComponentAlignment(opration, Alignment.MIDDLE_LEFT);
		header.setStyleName(ValoTheme.LABEL_COLORED);
		header.setSizeFull();
		receiptCode.setItems(ReceiptCode.values());
		// save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);

		// binder.bindInstanceFields(this);
		bindAllfiledsManually();

		// myUI.updateList(usersMap);
		save.addClickListener(e -> this.save());
		delete.addClickListener(e -> this.delete());
		refresh.addClickListener(e -> this.refresh());
		delete.setEnabled(false);
	}

	private void bindAllfiledsManually() {

		binder.forField(oprationCode).asRequired("mandatorry filed ").bind(OprationBean::getOprationCode,
				OprationBean::setOprationCode);
		binder.forField(oprationDesciption).asRequired("mandatorry filed ").bind(OprationBean::getOprationDesciption,
				OprationBean::setOprationDesciption);
		binder.forField(receiptCode).asRequired("mandatorry filed ").bind(OprationBean::getReceiptCode,
				OprationBean::setReceiptCode);

		binder.forField(price).withConverter(Double::valueOf, String::valueOf,
				// Text to use instead of the NumberFormatException message
				"Please enter a number").bind(OprationBean::getPrice, OprationBean::setPrice);
		binder.forField(defaultDLTCharge).withConverter(Double::valueOf, String::valueOf,
				// Text to use instead of the NumberFormatException message
				"Please enter a number").bind(OprationBean::getDLTCharge, OprationBean::setDLTCharge);
		binder.forField(defaultWage).withConverter(Double::valueOf, String::valueOf,
				// Text to use instead of the NumberFormatException message
				"Please enter a number").bind(OprationBean::getWage, OprationBean::setWage);

	}

	public void setCustomer(OprationBean customer) {
		this.oprationBean = customer;
		binder.setBean(customer);

		// Show delete button for only customers already in the database
		delete.setEnabled(customer.isPersisted());
		// setVisible(true);
		oprationCode.selectAll();
	}

	private void delete() {
		// oprationService.delete(customer);
		System.out.println("TESTING ID : " + oprationBean.getId());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(REST_SERVICE_URI + "/deleteOpration/" + oprationBean.getId());
		myUI.updateList();
	}

	private void save() {
		if (oprationBean.getOprationCode().isEmpty() || oprationBean.getOprationDesciption().isEmpty()
				|| !(Optional.ofNullable(oprationBean.getReceiptCode()).isPresent())) {
			return;
		}
		RestTemplate restTemplate = new RestTemplate();
		System.out.println("Created Value " + oprationBean.getReceiptCode() + " Id is : " + oprationBean.getId());
		oprationBean.setProvinceBean(Optional.ofNullable(myUI.getProvinceForm().getProvinceBeanBean()).orElse(new ProvinceBean()) );
		URI uri = restTemplate.postForLocation(REST_SERVICE_URI + "/createOpration/", oprationBean, OprationBean.class);
		myUI.getGrids().asSingleSelect().clear();
		setCustomer(new OprationBean());
		myUI.updateList();
	}

	private void refresh() {
		myUI.getGrids().asSingleSelect().clear();
		setCustomer(new OprationBean());
		myUI.updateList();
	}

	public List<OprationBean> UpdateOprationList() {
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI + "/getOprations/",
				List.class);
		List<OprationBean> oprationBeanList = new ArrayList<OprationBean>();
		if (usersMap != null) {
			for (LinkedHashMap<String, Object> map : usersMap) {
				OprationBean oprationBean = new OprationBean();
				System.out.println("recepent code in get  : " + map.get("receiptCode"));
				int intVal = (int) map.get("id");
				oprationBean.setId((long) intVal);
				oprationBean.setOprationCode(map.get("oprationCode") + "");
				oprationBean.setOprationDesciption(map.get("oprationDesciption") + "");
				if (ReceiptCode.EX_WORKS_001.toString().equals(map.get("receiptCode") + ""))
					oprationBean.setReceiptCode(ReceiptCode.EX_WORKS_001);
				else if (ReceiptCode.FREE_CARRIER_002.toString().equals(map.get("receiptCode") + ""))
					oprationBean.setReceiptCode(ReceiptCode.FREE_CARRIER_002);
				else if (ReceiptCode.CARRIAGE_PAID_TO_003.toString().equals(map.get("receiptCode") + ""))
					oprationBean.setReceiptCode(ReceiptCode.CARRIAGE_PAID_TO_003);
				else if (ReceiptCode.CARRIAGE_AND_INSURANCE_PAID_004.toString().equals(map.get("receiptCode") + ""))
					oprationBean.setReceiptCode(ReceiptCode.CARRIAGE_AND_INSURANCE_PAID_004);
				else if (ReceiptCode.DELIVERED_DUTY_PAID_005.toString().equals(map.get("receiptCode") + ""))
					oprationBean.setReceiptCode(ReceiptCode.DELIVERED_DUTY_PAID_005);

				oprationBean.setPrice((Double) map.get("price"));
				oprationBean.setDLTCharge((Double) map.get("dltcharge"));
				oprationBean.setWage((Double) map.get("wage"));
				oprationBeanList.add(oprationBean);
			}
		} else {
			System.out.println("No user exist----------");
		}
		return oprationBeanList;
	}

}
