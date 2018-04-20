package milu.gui.dlg.db;

import java.util.Map;

import milu.db.MyDBAbstract;

public interface UrlInterface
{
	public Map<String,String> getProp();
	public String setUrl( MyDBAbstract.UPDATE update );
}
