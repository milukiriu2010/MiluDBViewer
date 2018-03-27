package milu.gui.ctrl.common;

import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar;
import javafx.scene.Node;

// https://stackoverflow.com/questions/36663767/how-to-specify-a-particular-order-of-buttons-in-a-javafx-8-alert
public class ButtonOrderNoneDialogPane extends DialogPane 
{
    @Override
    protected Node createButtonBar() 
    {
        ButtonBar node = (ButtonBar) super.createButtonBar();
        node.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);
        return node;
    }	
}
