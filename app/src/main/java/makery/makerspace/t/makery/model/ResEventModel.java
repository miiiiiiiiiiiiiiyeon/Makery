package makery.makerspace.t.makery.model;

import java.util.ArrayList;

/**
 * Created by MIYEON on 2017-08-25.
 */

public class ResEventModel {
    ArrayList<EventRegionsModel> regions;
    ArrayList<EventEventsModel> events;

    public ArrayList<EventRegionsModel> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<EventRegionsModel> regions) {
        this.regions = regions;
    }

    public ArrayList<EventEventsModel> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventEventsModel> events) {
        this.events = events;
    }
}
