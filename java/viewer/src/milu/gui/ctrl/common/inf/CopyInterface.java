package milu.gui.ctrl.common.inf;

/************************************
 * Interface for Copy Table Data
 ************************************
 * @author milu
 *
 */
public interface CopyInterface
{
	/*********************************
	 * Copy Table Data Without Column
	 *********************************
	 */
	public void copyTableNoHead();
	
	/*********************************
	 * Copy Table Data With Column
	 *********************************
	 */
	public void copyTableWithHead();
}
