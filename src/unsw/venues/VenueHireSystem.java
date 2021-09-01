/**
 *
 */
package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 *
 */
public class VenueHireSystem {

    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
	ArrayList<Venue> venues;
    public VenueHireSystem() {
    	this.venues = new ArrayList<>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);

            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);
            System.out.println(result.toString(2));
            break;
        case "change":
			id = json.getString("id");
			start = LocalDate.parse(json.getString("start"));
			end = LocalDate.parse(json.getString("end"));
			small = json.getInt("small");
			medium = json.getInt("medium");
			large = json.getInt("large");

            result = change(id, start, end, small, medium, large);
            System.out.println(result.toString(2));
            break;
        // TODO Implement other commands
        case "cancel":
        	id = json.getString("id");
        	result = cancel(id);

        	System.out.println(result.toString(2));
        	break;
        case "list":
        	venue = json.getString("venue");
        	JSONArray li = list(venue);
        	System.out.println(li.toString(2));
        	break;
        default:
        	JSONObject re = new JSONObject();
        	re.put("status", "rejected");
        	System.out.println(re.toString(2));
        }
    }

   

    public JSONObject change(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
    	ArrayList<room> room_s = this.find_rooms(start, end, small, medium, large); // new rooms that avil

    	JSONObject result = new JSONObject();
    	if (room_s.size() == 0) {
    		result.put("status", "rejected");
    		return result;
    	}
    	room b_r = new room();
    	reservation reser = new reservation();

    	ArrayList<room> ro = new ArrayList<>();
    	ArrayList<reservation> reser_list = new ArrayList<>();

    	for(Venue v : this.venues) {
    		for(room r : v.getALLroom()) {
    			if(r.find_Reservation(id).getId() != null) {
    				b_r = r; //the room
    				reser = r.find_Reservation(id);
    				ro.add(b_r);
    				reser_list.add(reser);
    			}
    		}
    	}

    	if (ro.size() == 0) {
    		result.put("status", "rejected");
    		return result;
    	}

    	for(int i = 0; i <ro.size(); i++) {
    		ro.get(i).removeReservation(reser_list.get(i));							// remove Reservations
    	}

    	this.add_reservation(room_s, start, end, id);
      result.put("status", "success");
      result.put("venue", room_s.get(0).getVenue());

      JSONArray rooms = new JSONArray();
    	for(int i = 0; i <room_s.size(); i++) {
    		rooms.put(ro.get(i).getRoom_name());									// put in json array
    	}

        result.put("rooms", rooms);
        return result;

    }


    public JSONObject cancel(String id) {
    	JSONObject result = new JSONObject();

    	room b_r = new room();
    	reservation reser = new reservation();
    	ArrayList<room> ro = new ArrayList<>();
    	ArrayList<reservation> reser_list = new ArrayList<>();

    	for(Venue v : this.venues) {
    		for(room r : v.getALLroom()) {
    			if(r.find_Reservation(id).getId() != null) {
    				b_r = r; 														//the room that we book maybe more than one
    				reser = r.find_Reservation(id);
    				ro.add(b_r);													//the Reservation(s)
    				reser_list.add(reser);
    			}
    		}
    	}
    	if (ro.size() == 0 ) {
    		result.put("status", "rejected");
    		return result;
    	}

    	for(int i = 0; i <ro.size(); i++) {
    		ro.get(i).removeReservation(reser_list.get(i));
    	}
    	result.put("status", "success");
    	return result;
    }


    public JSONArray list(String venue) {
    	JSONArray jo =new JSONArray();

    	Venue n = this.find_venue(venue);
    	for (room r : n.getALLroom()) {
    		
    		JSONObject com = new JSONObject();
    		JSONArray re = new JSONArray();														// the list for the Reservation
    		ArrayList<reservation> re_list = r.sorted_list();
    
    		for (reservation re_in : re_list) {
    			JSONObject com_2 = new JSONObject(); 										// the object for the Reservation
    			com_2.put("id", re_in.getId());
    			com_2.put("start", re_in.getStartdate());
    			com_2.put("end", re_in.getEnddate());
    			re.put(com_2);
    		}
	    	com.put("room", r.getRoom_name());
	    	
    		com.put("reservations", re);
    		jo.put(com);

    	}
    	return  jo;
    }

    
    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        JSONObject result = new JSONObject();

        ArrayList<room> room_s = this.find_rooms(start, end, small, medium, large);

        if (room_s.size() == 0) {																//no available room or venue
        	result.put("status", "rejected");
        	return result;
        }
        this.add_reservation(room_s, start, end, id);


        result.put("status", "success");
        result.put("venue", room_s.get(0).getVenue());
        JSONArray rooms = new JSONArray();
        
        for(int i = room_s.size()-1; i >= 0; i--) {
        	rooms.put( room_s.get(i).getRoom_name());
        }
        
        result.put("rooms", rooms);
        return result;
    }

    private void addRoom(String venue, String room, String size) {
    	Venue v = find_venue(venue);
    	room e = new room(room, size,venue);
    	v.addRoom(e);
    }

    public Venue find_venue(String v) {
    	for (int i = 0; i < this.venues.size(); i++) {
    		if (this.venues.get(i).getName().equals(v)) {
    			return this.venues.get(i);
    		}
    	}
    	Venue e = new Venue(v);
    	this.venues.add(e);
    	return e;																	//return new one is there is none named v
    }

    
    // find the available rooms otherwise return an empty list
	public ArrayList<room> find_rooms(LocalDate start, LocalDate end,
			int small, int medium, int large) {
		ArrayList<room> rooms = new ArrayList<>();

		for(int i = 0; i < this.venues.size(); i++) {
			Venue v = this.venues.get(i);
			//first check the venue have enough rooms
			if (v.cheak_room(small, medium, large)){
				ArrayList<room> temp = new ArrayList<>();
				temp.addAll(v.find_large_rooms(start, end, large));
				temp.addAll(v.find_small_rooms(start, end, small));
				temp.addAll(v.find_medium_rooms(start, end, medium));
				if(temp.size() >=  (small+medium+large)) { 							//we have enough room
					rooms = temp;
					break;
				}
			}

		}

		return rooms;
	}

	//give the reservation to the list of room
	public void add_reservation(ArrayList<room> rooms, LocalDate start, LocalDate end, String ID) {
		for(int i = 0; i < rooms.size(); i++) {
			rooms.get(i).addReservation(new reservation (ID,start, end));

		}
	}
	

    
    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }
}
