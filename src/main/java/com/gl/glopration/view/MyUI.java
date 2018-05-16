package com.gl.glopration.view;

import java.awt.Label;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.springframework.beans.factory.annotation.Autowired;

import com.gl.glopration.model.OprationBean;
import com.gl.glopration.model.ProvinceBean;
import com.gl.glopration.model.ReceiptCode;
import com.gl.glopration.service.OprationService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
@SpringUI
public class MyUI extends UI {

	@Autowired
	OprationService oprationService;
	private Grid<OprationBean> grid = new Grid<>(OprationBean.class);
	private TextField filterText1 = new TextField();
	private TextField filterText2 = new TextField();
	private NativeSelect<ReceiptCode> receiptCode = new NativeSelect<>();
	private boolean exicuteOnce = true;

	private UserOprationForm mainOprationForm = new UserOprationForm(this);

	private ProvinceForm provinceForm = new ProvinceForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setColumns("oprationCode", "oprationDesciption", "receiptCode", "price", "DLTCharge", "wage");

		HeaderRow filteringHeader = grid.appendHeaderRow();
		filteringHeader.getCell("oprationCode").setComponent(filterText1);
		filteringHeader.getCell("oprationDesciption").setComponent(filterText2);
		filteringHeader.getCell("receiptCode").setComponent(receiptCode);
		receiptCode.setSizeFull();

		HorizontalLayout main = new HorizontalLayout(grid);
		HorizontalLayout forms = new HorizontalLayout(mainOprationForm, provinceForm);
		forms.setSizeFull();
		main.setSizeFull();
		grid.setSizeFull();
		forms.setExpandRatio(mainOprationForm, 1);

		layout.addComponents(forms, main);

		// fetch list of Customers from service and assign it to Grid
		updateList();

		setContent(layout);

		provinceForm.setVisible(false);
		if (exicuteOnce) {
			exicuteOnce = false;
			mainOprationForm.setCustomer(new OprationBean());
		}
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				provinceForm.setVisible(false);
			} else {
				mainOprationForm.setCustomer(event.getValue());
				provinceForm.setProvince(new ProvinceBean());
			}
		});
	}

	public void updateList() {
		List<OprationBean> customers = mainOprationForm.UpdateOprationList();
		grid.setItems(customers);
	}

	public Grid<OprationBean> getGrids() {
		return grid;
	}

	public UserOprationForm getForms() {
		return mainOprationForm;
	}
	
	public ProvinceForm getProvinceForm() {
		return provinceForm;
	}

}
