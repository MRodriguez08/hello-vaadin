package com.globant.vaadin.hellovaadin;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
@Widgetset("com.globant.vaadin.hellovaadin.MyAppWidgetset")
public class MyUI extends UI {

	private CustomerService customerService = CustomerService.getInstance();

	private Grid grid;
	
	private CustomerForm form;

	@Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        grid = new Grid();
        grid.setColumns("firstName", "email", "status");
        grid.setSizeFull();
        form = new CustomerForm(this);
        
        grid.addSelectionListener(event -> {
            if (event.getSelected().isEmpty()) {
                form.setVisible(false);
            } else {
                Customer customer = (Customer) event.getSelected().iterator().next();
                form.setCustomer(customer);
            }
        });
        
        TextField filterText = new TextField();
        filterText.setInputPrompt("filter by name...");
        
        filterText.addTextChangeListener(e -> {
        	updateGrid(e.getText());
        });
        
        Button filterButton = new Button(FontAwesome.TIMES);
        filterButton.addClickListener(e -> {
        	filterText.clear();
        	updateGrid("");
        });
        
        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.select(null);
            form.setCustomer(new Customer());
        });
        
        CssLayout filtering = new CssLayout();
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.addComponents(filterText, filterButton);
        
        HorizontalLayout toolbal = new HorizontalLayout(filtering, addCustomerBtn);
        toolbal.setSpacing(true);
        
        updateGrid("");
        
        layout.setMargin(true);
        layout.setSpacing(true);
        
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSpacing(true);
        main.setSizeFull();
        main.setExpandRatio(grid, 1);
        
        layout.addComponents(toolbal, main);
        setContent(layout);
        form.setVisible(false);
    }

	public void updateGrid(String filterText) {
		List<Customer> customers = customerService.findAll(filterText);
		grid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
