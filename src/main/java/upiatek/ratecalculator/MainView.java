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
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicReference;

@Route
public class MainView extends VerticalLayout {

    TextField sendLabel;
    TextField receiveLabel;
    CurrentRate currentRate;

    public MainView() throws IOException, JSONException {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setMargin(true);

        currentRate = new CurrentRate();
        currentRate.setRate("http://api.nbp.pl/api/exchangerates/rates/a/gbp/?format=json");

        setupComponents();
        calculateSendLayout();
        calculateReceiveLayout();
    }

    private void calculateSendLayout() {
        AtomicReference<String> value = new AtomicReference<>("");
        sendLabel.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            value.set(sendLabel.getValue());
            try {
                if (!String.valueOf(value).isEmpty()) {
                    Double.parseDouble(String.valueOf(value));
                    Double amount = 0.0;
                    amount = currentRate.calculate(1, String.valueOf(value));
                    receiveLabel.setValue(String.valueOf(amount));
                }
            } catch (NumberFormatException e) {
                dialogPopup();
            }

        });

    }

    private void calculateReceiveLayout() {
        AtomicReference<String> value = new AtomicReference<>("");
        receiveLabel.addValueChangeListener(textFieldStringComponentValueChangeEvent -> {
            value.set(receiveLabel.getValue());
            try {
                if (!String.valueOf(value).isEmpty()) {
                    Double.parseDouble(String.valueOf(value));
                    Double amount = 0.0;
                    amount = currentRate.calculate(2, String.valueOf(value));
                    sendLabel.setValue(String.valueOf(amount));
                }
            } catch (NumberFormatException e) {
                dialogPopup();
            }
        });

    }

    public void setupComponents() { //sets up all the UI components
        HorizontalLayout sendLayout = new HorizontalLayout();
        sendLayout.setAlignItems(Alignment.BASELINE);
        sendLabel = new TextField("You send");
        Image ukFlag = new Image("https://cdn.countryflags.com/thumbs/united-kingdom/flag-3d-250.png", "UKFlag");
        ukFlag.setWidth("60px");
        ukFlag.setHeight("30px");
        TextField currencyGBP = new TextField();
        currencyGBP.setValue("GBP");
        currencyGBP.setWidth("60px");
        currencyGBP.setReadOnly(true);
        sendLayout.add(ukFlag, sendLabel, currencyGBP);
        add(sendLayout);

        HorizontalLayout receiveLayout = new HorizontalLayout();
        receiveLayout.setAlignItems(Alignment.BASELINE);
        receiveLabel = new TextField();
        receiveLabel.setLabel("They receive");
        Image polandFlag = new Image("https://cdn.countryflags.com/thumbs/poland/flag-3d-250.png", "PolandFlag");
        polandFlag.setWidth("60px");
        polandFlag.setHeight("30px");
        TextField currencyPLN = new TextField();
        currencyPLN.setValue("PLN");
        currencyPLN.setWidth("60px");
        currencyPLN.setReadOnly(true);
        receiveLayout.add(polandFlag, receiveLabel, currencyPLN);
        add(receiveLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Text text = new Text("1 GBP = " + currentRate.rate + " PLN");
        horizontalLayout.add(text);
        add(horizontalLayout);
    }

    public void dialogPopup() {
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
        sendLabel.clear();
        receiveLabel.clear();
    }
}
