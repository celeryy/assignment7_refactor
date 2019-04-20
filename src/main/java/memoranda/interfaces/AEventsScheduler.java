/**
 * EventsScheduler.java
 * Created on 10.03.2003, 20:20:08 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package main.java.memoranda.interfaces;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 */
/*$Id: EventsScheduler.java,v 1.4 2004/01/30 12:17:41 alexeya Exp $*/
public class AEventsScheduler {

    static Vector _timers = new Vector();
    static Vector _listeners = new Vector();

    static Timer changeDateTimer = new Timer();

    static {
        addListener(new IDefaultEventNotifier());            
    }

    public static void init() {
        cancelAll();
        //changeDateTimer.cancel();
        Vector events = (Vector)AEventsManager.getActiveEvents();
        _timers = new Vector();
        /*DEBUG*/System.out.println("----------");
        for (int i = 0; i < events.size(); i++) {
            AEvent ev = (AEvent)events.get(i);
            Date evTime = ev.getTime();
        /*DEBUG*/System.out.println((Calendar.getInstance()).getTime());
          //  if (evTime.after(new Date())) {
	      if (evTime.after((Calendar.getInstance()).getTime())) {	
                EventTimer t = new EventTimer(ev);
                t.schedule(new NotifyTask(t), ev.getTime());                
                _timers.add(t);
                /*DEBUG*/System.out.println(ev.getTimeString());
            }
        }
        /*DEBUG*/System.out.println("----------");
        Date midnight = getMidnight();
        changeDateTimer.schedule(new TimerTask() {
                public void run() {
                    init();
                    this.cancel();
                }
        }, midnight);
        notifyChanged();
    }

    public static void cancelAll() {
        for (int i = 0; i < _timers.size(); i++) {
            EventTimer t = (EventTimer)_timers.get(i);
            t.cancel();
        }
    }
    
    public static Vector getScheduledEvents() {
        Vector v = new Vector();
        for (int i = 0; i < _timers.size(); i++) 
            v.add(((EventTimer)_timers.get(i)).getEvent());
        return v;
    }
    
    public static AEvent getFirstScheduledEvent() {
        if (!isEventScheduled()) return null;
        AEvent e1 = ((EventTimer)_timers.get(0)).getEvent();
        for (int i = 1; i < _timers.size(); i++) { 
            AEvent ev = ((EventTimer)_timers.get(i)).getEvent();
            if (ev.getTime().before(e1.getTime()))
                e1 = ev;
        }
        return e1;
    }
            

    public static void addListener(IEventNotificationListener enl) {
        _listeners.add(enl);
    }

    public static boolean isEventScheduled() {
        return _timers.size() > 0;
    }
        
    private static void notifyListeners(AEvent ev) {
        for (int i = 0; i < _listeners.size(); i++)
            ((IEventNotificationListener)_listeners.get(i)).eventIsOccured(ev);
    }

    private static void notifyChanged() {
        for (int i = 0; i < _listeners.size(); i++)
            ((IEventNotificationListener)_listeners.get(i)).eventsChanged();
    }

    private static Date getMidnight() {
       Calendar cal = Calendar.getInstance();
       cal.set(Calendar.HOUR_OF_DAY, 0);
       cal.set(Calendar.MINUTE, 0);
       cal.set(Calendar.SECOND,0);
       cal.set(Calendar.MILLISECOND,0);
       cal.add(Calendar.DAY_OF_MONTH,1);
       return cal.getTime();
    }

    static class NotifyTask extends TimerTask {
        
        EventTimer _timer;

        public NotifyTask(EventTimer t) {
            super();            
            _timer = t;
        }
        
        public void run() {            
            _timer.cancel();
            _timers.remove(_timer);
            notifyListeners(_timer.getEvent());
            notifyChanged();
        }
    }
    
    static class EventTimer extends Timer {
        AEvent _event;
        
        public EventTimer(AEvent ev) {
            super();
            _event = ev;
        }
        
        public AEvent getEvent() {
            return _event;
        }
    }


}
