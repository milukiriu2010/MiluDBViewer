package milu.task.main;

import java.util.Map;

import milu.main.MainController;
import milu.task.ProgressInterface;

abstract public class InitialLoadAbstract 
{
	protected MainController  mainCtrl = null;
	
	protected Map<String,String>  propMap = null;
	
	protected ProgressInterface  progressInf = null;
	
	protected double          assignedSize = 0.0;
	
	public void setMainController( MainController mainCtrl )
	{
		this.mainCtrl = mainCtrl;
	}
	
	public void setPropMap( Map<String,String> propMap )
	{
		this.propMap = propMap;
	}
	
	void setProgressInterface( ProgressInterface progressInf )
	{
		this.progressInf = progressInf;
	}
	
	void setAssignedSize( double assignedSize )
	{
		this.assignedSize = assignedSize;
	}

	abstract public void load();
}
