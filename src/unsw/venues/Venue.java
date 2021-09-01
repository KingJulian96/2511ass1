package unsw.venues;
import java.time.LocalDate;
import java.util.ArrayList;

public class Venue {

	private String name;
	private ArrayList<room> large_room;
	private ArrayList<room> small_room;
	private ArrayList<room> medium_room;
	
	public Venue(String name) {
		this.name = name;
		this.small_room = new ArrayList<>();
		this.medium_room = new ArrayList<>();
		this.large_room = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<room> getLarge_room() {
		return large_room;
	}
	

	public ArrayList<room> getALLroom () {
		ArrayList<room>list = new ArrayList<>();
		list.addAll(this.small_room);
		list.addAll(this.medium_room);
		list.addAll(this.large_room);
		return list;
	}
		
	
	public void setLarge_room(ArrayList<room> large_room) {
		this.large_room = large_room;
	}


	public ArrayList<room> getSmall_room() {
		return small_room;
	}


	public void setSmall_room(ArrayList<room> small_room) {
		this.small_room = small_room;
	}


	public ArrayList<room> getMedium_room() {
		return medium_room;
	}


	public void setMedium_room(ArrayList<room> medium_room) {
		this.medium_room = medium_room;
	}

	

	public void addRoom(room r) {
		if(r.getSize().equals("small")) this.small_room.add(r);
		if(r.getSize().equals("large")) this.large_room.add(r);
		if(r.getSize().equals("medium")) this.medium_room.add(r);
	}
	
	// cheak if there are "int small, int medium, int large" in this venue
	public boolean cheak_room(int small, int medium, int large) {
		if (small > this.getSmall_room().size()) {
			return false;
		}
		if (large > this.getLarge_room().size()) {
			return false;
		}
		if (medium > this.getMedium_room().size()) {
			return false;
		}
		return true;
	}
	
	public ArrayList<room> find_small_rooms(LocalDate start, LocalDate end, int small){
		ArrayList<room> rooms = new ArrayList<>();
		for (int i = 0; i < this.small_room.size() && rooms.size() != small; i++) {
			room c_r = this.small_room.get(i);
			if(c_r.cheak_time(start, end)) {
				rooms.add(c_r);
			}
		}
		return rooms;
	}
	public ArrayList<room> find_medium_rooms(LocalDate start, LocalDate end, int medium){
		ArrayList<room> rooms = new ArrayList<>();
		for (int i = 0; i < this.medium_room.size() && rooms.size() != medium; i++) {
			room c_r = this.medium_room.get(i);
			if(c_r.cheak_time(start, end)) {
				rooms.add(c_r);
			}
		}
		return rooms;
	}
	
	public ArrayList<room> find_large_rooms(LocalDate start, LocalDate end, int large){
		ArrayList<room> rooms = new ArrayList<>();
		for (int i = 0; i < this.large_room.size() && rooms.size() != large; i++) {
			room c_r = this.large_room.get(i);
			if(c_r.cheak_time(start, end)) {
				rooms.add(c_r);
			}
		}
		return rooms;
	}
}
