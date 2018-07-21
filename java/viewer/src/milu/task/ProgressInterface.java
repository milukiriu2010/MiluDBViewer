package milu.task;

public interface ProgressInterface 
{
	public void addProgress( double addpos );
	public void setProgress( double pos );
	public void setMsg( String msg );
	public void cancelProc();
}
