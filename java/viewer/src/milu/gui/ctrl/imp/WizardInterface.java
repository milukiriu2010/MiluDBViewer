package milu.gui.ctrl.imp;

import java.util.Map;
import javafx.scene.layout.Pane;

public interface WizardInterface
{
	public void next( Pane pane, Map<String,Object> mapObj );
	public void prev();
	public void close();
	public void setMsg( String msg );
}
