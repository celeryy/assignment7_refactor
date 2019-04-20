/**
 * ProjectImpl.java
 * Created on 11.02.2003, 23:06:22 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.interfaces;

import main.java.memoranda.date.CalendarDate;
import main.java.memoranda.date.CurrentDate;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Default implementation of Project interface
 */
/*$Id: ProjectImpl.java,v 1.7 2004/11/22 10:02:37 alexeya Exp $*/
public class IProjectImpl implements AProject {

    private Element _root = null;

    /**
     * Constructor for ProjectImpl.
     */
    public IProjectImpl(Element root) {        
        _root = root;
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#getID()
     */
    public String getID() {
        return _root.getAttribute("id").getValue();
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#getStartDate()
     */
    public CalendarDate getStartDate() {
        Attribute d = _root.getAttribute("startDate");
        if (d == null) return null;
        return new CalendarDate(d.getValue());        
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#setStartDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setStartDate(CalendarDate date) {
        if (date != null)
            setAttr("startDate", date.toString());
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#getEndDate()
     */
    public CalendarDate getEndDate() {
        Attribute d = _root.getAttribute("endDate");
        if (d == null) return null;
        return new CalendarDate(d.getValue());
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#setEndDate(net.sf.memoranda.util.CalendarDate)
     */
    public void setEndDate(CalendarDate date) {
        if (date != null)
            setAttr("endDate", date.toString());
        else if (_root.getAttribute("endDate") != null)
            setAttr("endDate", null);
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#getStatus()
     */
    public int getStatus() {
        if (isFrozen())
            return AProject.FROZEN;
        CalendarDate today = CurrentDate.get();
        CalendarDate prStart = getStartDate();
        CalendarDate prEnd = getEndDate();
        if (prEnd == null) {
            if (today.before(prStart))
                return AProject.SCHEDULED;
            else
                return AProject.ACTIVE;                
        }    
        if (today.inPeriod(prStart, prEnd))
            return AProject.ACTIVE;
        else if (today.after(prEnd)) {
            //if (getProgress() == 100)
                return AProject.COMPLETED;
            /*else
                return Project.FAILED;*/
        }
        else
            return AProject.SCHEDULED;
    }

    private boolean isFrozen() {
        return _root.getAttribute("frozen") != null;
    }

   
    /*public int getProgress() {
        Vector v = getAllTasks();
        if (v.size() == 0) return 0;
        int p = 0;
        for (Enumeration en = v.elements(); en.hasMoreElements();) {
          Task t = (Task) en.nextElement();
          p += t.getProgress();
        }
        return (p*100)/(v.size()*100);
    }*/
  
    
    /**
     * @see main.java.memoranda.interfaces.AProject#freeze()
     */
    public void freeze() {
        _root.addAttribute(new Attribute("frozen", "yes"));
    }

    /**
     * @see main.java.memoranda.interfaces.AProject#unfreeze()
     */
    public void unfreeze() {
        if (this.isFrozen())
            _root.removeAttribute(new Attribute("frozen", "yes"));
    }
    
    /**
     * @see main.java.memoranda.interfaces.AProject#getTitle()
     */
    public String getTitle() {
        Attribute ta = _root.getAttribute("title");
        if (ta != null)
            return ta.getValue();
        return "";
    }
    /**
     * @see main.java.memoranda.interfaces.AProject#setTitle(java.lang.String)
     */
    public void setTitle(String title) {
        setAttr("title", title);
    }
    
    private void setAttr(String name, String value) {
        Attribute a = _root.getAttribute(name);
        if (a == null) {
            if (value != null)
             _root.addAttribute(new Attribute(name, value));
        }
        else if (value != null)        
            a.setValue(value);
        else 
            _root.removeAttribute(a);
    }

	public String getDescription() {
    	Element thisElement = _root.getFirstChildElement("description");
    	if (thisElement == null) {
    		return null;
    	}
    	else {
       		return thisElement.getValue();
    	}
    }

    public void setDescription(String s) {
    	Element desc = _root.getFirstChildElement("description");
    	if (desc == null) {
        	desc = new Element("description");
            desc.appendChild(s);
            _root.appendChild(desc);    	
    	}
    	else {
            desc.removeChildren();
            desc.appendChild(s);    	
    	}
    }
        
    /**
     * @see net.sf.memoranda.Project#getTaskList()
     */
    /*public TaskList getTaskList() {
        return CurrentStorage.get().openTaskList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getNoteList()
     */
    /*public NoteList getNoteList() {
        return CurrentStorage.get().openNoteList(this);
    }*/
    /**
     * @see net.sf.memoranda.Project#getResourcesList()
     */
    /*public ResourcesList getResourcesList() {
        return CurrentStorage.get().openResourcesList(this);
    }*/
}
