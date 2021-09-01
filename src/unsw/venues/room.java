package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;

public class room{
	

	private String size;
	private String room_name;
	private String Venue;
	private ArrayList<reservation> reservation;
	
	public room (String name, String size, String Venue) {
		if (size.equals("small") || size.equals("large") ||size.equals("medium")) {
			this.room_name = name;
			this.size = size;
			this.Venue = Venue;
			this.reservation = new ArrayList<>();
		}else {
			System.out.println("error in creating room with " +size);
		}
	}
	public room () {
		
	}
	public ArrayList<reservation> getReservation() {
		return reservation;
	}

	public void setReservation(ArrayList<reservation> reservation) {
		this.reservation = reservation;
	}

	public void addReservation(reservation e) {
		this.reservation.add(e);
	}
	
	public void removeReservation(reservation e) {
		if (this.reservation.contains(e)) {
			this.reservation.remove(e);
		}
	}
	
	public reservation find_Reservation(String ID) {
		reservation e = new reservation();
		for(int i = 0; i < this.reservation.size(); i++) {
			if (this.reservation.get(i).getId().equals(ID)) e = this.reservation.get(i);
		}
		return e;
	}
	
	public String getVenue() {
		return Venue;
	}

	public void setVenue(String venue) {
		Venue = venue;
	}

	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}


	public String getRoom_name() {
		return room_name;
	}
	
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	
	//Check if there is time left for booking between start and end
	public boolean cheak_time(LocalDate start, LocalDate end) {
		if (this.getReservation().size() == 0) {
			return true;
		}
		ArrayList<reservation> list = this.getReservation();
		for (int i = 0; i < list.size();i++) {
			LocalDate r_s = list.get(i).getStartdate();
			LocalDate r_e = list.get(i).getEnddate();
			
			if (this.isBetween(start, end, r_s)) return false;	//this is a event between start and end
			if (this.isBetween(start, end, r_e)) return false;
		
			if (this.isBetween(r_s,r_e , start)) return false;	//this is a event between start and end
			if (this.isBetween(r_s,r_e, end)) return false;
		}
		return true;
	}
	
	public boolean isBetween(LocalDate start, LocalDate end, LocalDate test) {
		if (start.isBefore(test) && end.isAfter(test)) {
			return true;
		}
		if (start.isEqual(test) || end.isEqual(test)) {
			return true;
		}
		return false;
	}


	
	public reservation early_one(ArrayList<reservation> re_list) {
		reservation n = null;

		for(int i= 0; i < re_list.size(); i++) {
			reservation temp = re_list.get(i);
			if (n == null) {
				n = temp;
			}
			else if (temp.getStartdate().isBefore(n.getStartdate())) {
				n = temp;
			}
		}
		return n;
	}
	
	public ArrayList<reservation> sorted_list(){
		ArrayList<reservation> l = this.reservation;
		ArrayList<reservation> new_list = new ArrayList<reservation>();
		while(!l.isEmpty()){
			reservation temp = this.early_one(l);
			//System.out.println(temp.getStartdate());
			new_list.add(temp);
			l.remove(temp);
		}
		this.reservation = new_list;
		return new_list;
	}
	

}
