package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.model.exception.StopException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Manages all bus stops.
 *
 * Singleton pattern applied to ensure only a single instance of this class that
 * is globally accessible throughout application.
 */

public class StopManager implements Iterable<Stop> {
    public static final int RADIUS = 10000;
    private static StopManager instance;
    // Use this field to hold all of the stops.
    // Do not change this field or its type, as the iterator method depends on it
    private Map<Integer, Stop> stopMap = new HashMap<>();
    private Stop selected;

    /**
     * Constructs stop manager with empty collection of stops and null as the selected stop
     */
    private StopManager() {
        stopMap = new HashMap<Integer, Stop>();
    }

    /**
     * Gets one and only instance of this class
     *
     * @return  instance of class
     */
    public static StopManager getInstance() {
        // Do not modify the implementation of this method!
        if(instance == null) {
            instance = new StopManager();
        }

        return instance;
    }

    public Stop getSelected() {
        return selected;
    }

    /**
     * Get stop with given id, creating it if necessary. If it is necessary to create a new stop,
     * then provide it with an empty string as its name, and a default location somewhere in the
     * lower mainland as its location.
     *
     * In this case, the correct name and location of the stop will be provided later
     *
     * @param id  the id of this stop
     *
     * @return  stop with given id
     */
    public Stop getStopWithId(int id) {
        if (!stopMap.containsKey(id)) {
            Stop s = new Stop(id, "", new LatLon(24.44, 33.56));
            stopMap.put(id, s);
            return s;
        }
        return stopMap.get(id);
    }

    /**
     * Get stop with given id, creating it if necessary, using the given name and latlon
     *
     * @param id  the id of this stop
     *
     * @return  stop with given id
     */
    public Stop getStopWithId(int id, String name, LatLon locn) {
        if (!stopMap.containsKey(id)) {
            Stop s = new Stop(id, name, locn);
            stopMap.put(id, s);
            return s;
        }
        return stopMap.get(id);
    }

    /**
     * Set the stop selected by user
     *
     * @param selected   stop selected by user
     * @throws StopException when stop manager doesn't contain selected stop
     */
    public void setSelected(Stop selected) throws StopException {
        if (!stopMap.containsValue(selected)) {
            throw new StopException("No such stop: " + selected.getNumber() + " " + selected.getName());
        }
        this.selected = selected;
    }

    /**
     * Clear selected stop (selected stop is null)
     */
    public void clearSelectedStop() {
        this.selected = null;
    }

    /**
     * Get number of stops managed
     *
     * @return  number of stops added to manager
     */
    public int getNumStops() {
        return stopMap.size();
    }

    /**
     * Remove all stops from stop manager
     */
    public void clearStops() {
        stopMap.clear();
    }

    /**
     * Find nearest stop to given point.  Returns null if no stop is closer than RADIUS metres.
     *
     * @param pt  point to which nearest stop is sought
     * @return    stop closest to pt but less than 10,000m away; null if no stop is within RADIUS metres of pt
     */
    public Stop findNearestTo(LatLon pt) {
        double D = 1000000;
        Stop nearest = new Stop(0, "", new LatLon(22.44, 33.56));
        for (Stop s : new ArrayList<>(stopMap.values())) {
            double distance = Math.sqrt(((s.getLocn().getLatitude() - pt.getLatitude()) * (s.getLocn().getLatitude() - pt.getLatitude()))
                    + ((s.getLocn().getLongitude() - pt.getLongitude()) * (s.getLocn().getLongitude() - pt.getLongitude())));
            if (distance < D) {
                D = distance;
                nearest = s;
            }
        }
        if (D * 111 > 10) {
            return null;
        }
        return nearest;
    }

    @Override
    public Iterator<Stop> iterator() {
        // Do not modify the implementation of this method!
        return stopMap.values().iterator();
    }

}
