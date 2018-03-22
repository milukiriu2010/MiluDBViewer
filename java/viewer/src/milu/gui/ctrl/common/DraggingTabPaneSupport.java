package milu.gui.ctrl.common;

import java.util.concurrent.atomic.AtomicLong;

import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Screen;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

// https://stackoverflow.com/questions/41473987/how-to-drag-and-drop-tabs-of-the-same-tabpane?rq=1
public class DraggingTabPaneSupport 
{
    private Tab currentDraggingTab ;

    private static final AtomicLong idGenerator = new AtomicLong();

    private final String draggingID = "DraggingTabPaneSupport-"+idGenerator.incrementAndGet() ;

    public void addSupport(TabPane tabPane) {
        tabPane.getTabs().forEach(this::addDragHandlers);
        tabPane.getTabs().addListener((Change<? extends Tab> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(this::addDragHandlers);
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(this::removeDragHandlers);
                }
            }
        });

        // if we drag onto a tab pane (but not onto the tab graphic), add the tab to the end of the list of tabs:
        tabPane.setOnDragOver(e -> {
            if (draggingID.equals(e.getDragboard().getString()) && 
                    currentDraggingTab != null &&
                    currentDraggingTab.getTabPane() != tabPane) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });
        tabPane.setOnDragDropped(e -> {
            if (draggingID.equals(e.getDragboard().getString()) && 
                    currentDraggingTab != null &&
                    currentDraggingTab.getTabPane() != tabPane) {

                currentDraggingTab.getTabPane().getTabs().remove(currentDraggingTab);
                tabPane.getTabs().add(currentDraggingTab);
                currentDraggingTab.getTabPane().getSelectionModel().select(currentDraggingTab);
            }
        });
    }

    private void addDragHandlers( Tab tab ) 
    {
        // move text to label graphic:
        if ( tab.getText() != null && !tab.getText().isEmpty() )
        {
            Label label = new Label(tab.getText(), tab.getGraphic());
            tab.setText(null);
            tab.setGraphic(label);
        }
        
        Node graphic = tab.getGraphic();
        tab.setOnSelectionChanged
        (
        	(event)->
        	{
        		Tab selectedTab = (Tab)event.getSource();
        		//System.out.println( "Selected Tab[" + selectedTab.getText() + "]" );
        		TabPane tabPane = selectedTab.getTabPane();
        		for ( Tab eachTab : tabPane.getTabs() )
        		{
        			// Set tooltip for unselected Tab
        			if ( eachTab.isSelected() == false )
        			{
        				if ( eachTab.getTooltip() == null )
        				{
        					// Graphic on Tab
	        				//Node graphicOnTab = eachTab.getGraphic();
	        				// Content on Tab
	    					Node contentOnTab = eachTab.getContent();
	    					
	    					// Screen Size
	    					Rectangle2D  rec = Screen.getPrimary().getBounds();
	    					
	    					// Capture Image
	    					WritableImage imgOnTab = new WritableImage( (int)rec.getWidth()/3, (int)rec.getHeight()/3 );
	    					contentOnTab.snapshot( null, imgOnTab );
	    					
	    					// set tooltip for tab
	    					ImageView ivOnTab = new ImageView( imgOnTab );
	        				Tooltip toolTip = new Tooltip();
	        				toolTip.setGraphic( ivOnTab );
	        				// 300ms
	        				toolTip.setShowDelay( new Duration( 300 ) );
	        				eachTab.setTooltip( toolTip );
	        				
	        				
	        				System.out.println( "EachTab: Tooltip set." );
        				}
        			}
        			// Clear tooltip for selected Tab
        			else
        			{
        				eachTab.setTooltip( null );
        			}
        		}
        	}
        );

        graphic.setOnDragDetected
        (
        	(e)->
        	{
        		System.out.println( "Tab:DragDetected." );
	            Dragboard dragboard = graphic.startDragAndDrop(TransferMode.MOVE);
	            ClipboardContent content = new ClipboardContent();
	            // dragboard must have some content, but we need it to be a Tab, which isn't supported
	            // So we store it in a local variable and just put arbitrary content in the dragbaord:
	            content.putString(draggingID);
	            dragboard.setContent(content);
	            dragboard.setDragView(graphic.snapshot(null, null));
	            currentDraggingTab = tab ;
        	}
        );
        
        graphic.setOnDragOver
        (
        	(e)->
        	{
        		//System.out.println( "Tab:DragOver." );
	        	/**/
	            if ( draggingID.equals(e.getDragboard().getString()) && 
	                 currentDraggingTab != null &&
	                 currentDraggingTab.getGraphic() != graphic ) 
	            {
	                e.acceptTransferModes(TransferMode.MOVE);
	            }
	            /**/
	        	//e.acceptTransferModes(TransferMode.MOVE);
        	}
        );
        
        graphic.setOnDragDropped
        (
        	(e)->
        	{
	            if ( draggingID.equals(e.getDragboard().getString()) && 
	                 currentDraggingTab != null &&
	                 currentDraggingTab.getGraphic() != graphic ) 
	            {
	                System.out.println( "Tab:DragDroppped." );
	                int index = tab.getTabPane().getTabs().indexOf(tab) ;
	                currentDraggingTab.getTabPane().getTabs().remove(currentDraggingTab);
	                tab.getTabPane().getTabs().add(index, currentDraggingTab);
	                currentDraggingTab.getTabPane().getSelectionModel().select(currentDraggingTab);
	            }
        	}
        );
        
        graphic.setOnDragDone(e -> currentDraggingTab = null);
    }

    private void removeDragHandlers(Tab tab) 
    {
        tab.getGraphic().setOnDragDetected(null);
        tab.getGraphic().setOnDragOver(null);
        tab.getGraphic().setOnDragDropped(null);
        tab.getGraphic().setOnDragDone(null);
    }
}
