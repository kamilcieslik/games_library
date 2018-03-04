package javafx;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ListenerMethods {
    public ListenerMethods() {

    }

    public void changeLabelTextField(String regex, TextField textField, Label label, String isEmpty, String doesNotFit) {
        if (textField.getText().isEmpty())
            label.setText(isEmpty);
        else if (!textField.getText().matches(regex))
            label.setText(doesNotFit);
        else
            label.setText("");
    }

    public void changeLabelComboBox(ComboBox comboBox, Label label, String isEmpty) {
        if (comboBox.getSelectionModel().getSelectedItem() == null)
            label.setText(isEmpty);
        else
            label.setText("");
    }
}
