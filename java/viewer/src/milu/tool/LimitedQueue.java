package milu.tool;

import java.util.LinkedList;

// https://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java
public class LimitedQueue<E> extends LinkedList<E> 
{
	private static final long serialVersionUID = 1L;
	
    private int limit = 0;

    public LimitedQueue(int limit) 
    {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) 
    {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
