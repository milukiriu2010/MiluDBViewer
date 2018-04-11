package milu.gui.ctrl.common;

import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.collections.ListChangeListener;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

// https://stackoverflow.com/questions/46835087/prevent-a-toggle-group-from-not-having-a-toggle-selected-java-fx?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
public class PersistentButtonToggleGroup extends ToggleGroup 
{
	public PersistentButtonToggleGroup() 
	{
		super();
		this.getToggles().addListener
		(
			new ListChangeListener<Toggle>() 
			{
		        @Override 
		        public void onChanged(Change<? extends Toggle> c) 
		        {
			        while (c.next()) 
			        {
			        	for (final Toggle addedToggle : c.getAddedSubList()) 
			        	{
			        		((ToggleButton) addedToggle).addEventFilter
			        		(
			        			MouseEvent.MOUSE_RELEASED, 
			        			new EventHandler<MouseEvent>() 
			        			{
			        				@Override 
			        				public void handle(MouseEvent mouseEvent) 
			        				{
			        					if (addedToggle.equals(getSelectedToggle())) 
			        					{
			        						mouseEvent.consume();
			        					}
			        				}
			        			}
			        		);
			          }
			        }
		        }
		    }
		);
	  }
}
