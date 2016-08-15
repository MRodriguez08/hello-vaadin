package com.globant.vaadin.hellovaadin;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CustomerForm extends FormLayout {
	
	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");
	private TextField email = new TextField("Email");
	private NativeSelect status = new NativeSelect("Status");
	private PopupDateField birthdate = new PopupDateField("Birthday");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	private MyUI myUi;
	private Customer customer;
	private CustomerService customerService = CustomerService.getInstance();
	
	public CustomerForm(MyUI myUi) {
		this.myUi = myUi;
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(save, delete);
		buttons.setSpacing(true);
		
		status.addItems(CustomerStatus.values());
		
		save.addClickListener(e->this.save());
		delete.addClickListener(e->this.delete());
		
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(KeyCode.ENTER);
		
		addComponents(firstName, lastName, email, status, birthdate, buttons);
	}
	
	public void setCustomer(Customer customer) {
	    this.customer = customer;
	    BeanFieldGroup.bindFieldsUnbuffered(customer, this);

	    // Show delete button for only customers already in the database
	    delete.setVisible(customer.isPersisted());
	    setVisible(true);
	    firstName.selectAll();
	}
	
	private void delete() {
		customerService.delete(customer);
	    myUi.updateGrid("");
	    setVisible(false);
	}

	private void save() {
		customerService.save(customer);
		myUi.updateGrid("");
	    setVisible(false);
	}

}
