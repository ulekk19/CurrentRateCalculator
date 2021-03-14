package upiatek.ratecalculator;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Route
public class MainView extends VerticalLayout {

    TextField gbpTextField;
    TextField plnTextField;
    CurrentRate currentRate;

    public MainView() throws IOException, JSONException {
        //set default UI view
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setMargin(true);

        //set url for current rate
        currentRate = new CurrentRate();
        currentRate.setRate("http://api.nbp.pl/api/exchangerates/rates/a/gbp/?format=json");

        setupComponents();
        calculateSendLayout();
        calculateReceiveLayout();
    }

    private void setupComponents() {
        //sets up all the UI components
        HorizontalLayout sendLayout = new HorizontalLayout();
        sendLayout.setAlignItems(Alignment.BASELINE);
        gbpTextField = new TextField("You send");
        gbpTextField.setId("gbp");
        Image ukFlag = new Image("https://cdn.countryflags.com/thumbs/united-kingdom/flag-3d-250.png", "UKFlag");
        ukFlag.setWidth("60px");
        ukFlag.setHeight("30px");
        TextField currencyGBP = new TextField();
        currencyGBP.setValue("GBP");
        currencyGBP.setWidth("60px");
        currencyGBP.setReadOnly(true);
        sendLayout.add(ukFlag, gbpTextField, currencyGBP);
        add(sendLayout);

        HorizontalLayout receiveLayout = new HorizontalLayout();
        receiveLayout.setAlignItems(Alignment.BASELINE);
        plnTextField = new TextField("They receive");
        plnTextField.setId("pln");
        Image polandFlag = new Image("https://cdn.countryflags.com/thumbs/poland/flag-3d-250.png", "PolandFlag");
        polandFlag.setWidth("60px");
        polandFlag.setHeight("30px");
        TextField currencyPLN = new TextField();
        currencyPLN.setValue("PLN");
        currencyPLN.setWidth("60px");
        currencyPLN.setReadOnly(true);
        receiveLayout.add(polandFlag, plnTextField, currencyPLN);
        add(receiveLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Text text = new Text("1 GBP = " + currentRate.getRate() + " PLN");
        horizontalLayout.add(text);
        add(horizontalLayout);
    }

    private void dialogPopup() {
        //create dialog in case of incorrect input
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        Dialog dialog = new Dialog();
        dialog.open();
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        dialog.setWidth("300px");
        dialog.setHeight("100px");
        dialog.add(
                new Text("Incorrect number format  "),
                new Button("Close", event -> {
                    dialog.close();
                }));
        //clear both text fields
        gbpTextField.clear();
        plnTextField.clear();
    }

    private void calculateSendLayout() {
        //get value from text field and calculate rate
        //choice 1 - calculate GBP to PLN
        gbpTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            calculateFromTextField(gbpTextField.getValue(), 1, plnTextField);
        });
    }

    private void calculateReceiveLayout() {
        //get value from text field and calculate rate
        //choice 2 - calculate PLN to GBP
        plnTextField.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            calculateFromTextField(plnTextField.getValue(), 2, gbpTextField);
        });
    }

    private void calculateFromTextField(String value, int i, TextField textField) {
        try {
            if (!value.isEmpty()) {
                value = value.replaceAll("-", "");
                Double amount = currentRate.calculate(i, Double.parseDouble(value));
                textField.setValue(String.valueOf(amount));
            }
        } catch (NumberFormatException e) {
            dialogPopup();
        }
    }
}