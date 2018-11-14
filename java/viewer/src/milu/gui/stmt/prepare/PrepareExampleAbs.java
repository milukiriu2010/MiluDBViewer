package milu.gui.stmt.prepare;

import java.util.List;

public abstract class PrepareExampleAbs 
{
	abstract public String getSQL();
	abstract public List<Object> getHeadLst();
	abstract public List<List<Object>> getDataRowLst();
}
