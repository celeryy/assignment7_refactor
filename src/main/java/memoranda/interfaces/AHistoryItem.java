/**
 * HistoryItem.java
 * Created on 07.03.2003, 18:31:39 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.interfaces;

import main.java.memoranda.date.CalendarDate;

/**
 * 
 */
/*$Id: HistoryItem.java,v 1.4 2004/10/06 19:15:43 ivanrise Exp $*/
public class AHistoryItem {
    
    private CalendarDate _date;
    private AProject _project;
    /**
     * Constructor for HistoryItem.
     */
    public AHistoryItem(CalendarDate date, AProject project) {
        _date = date;
        _project = project;
    }
    
    public AHistoryItem(INote note) {
        _date = note.getDate();
        _project = note.getProject();
    }
    
    public CalendarDate getDate() {
       return _date;
    }
    
    public AProject getProject() {
       return _project;
    }
    
    public boolean equals(AHistoryItem i) {
       return i.getDate().equals(_date) && i.getProject().getID().equals(_project.getID());
    } 

}
