package unsw.venues;
import java.time.LocalDate;


public class reservation {

	private String id;
	private LocalDate startdate;
	private LocalDate enddate;


	public reservation(String ID, LocalDate startdate, LocalDate enddate){
		this.id = ID;
		this.enddate= enddate;
		this.startdate = startdate;
	}

	public reservation(){

	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDate getStartdate() {
		return startdate;
	}
	public void setStartdate(LocalDate startdate) {
		this.startdate = startdate;
	}
	public LocalDate getEnddate() {
		return enddate;
	}
	public void setEnddate(LocalDate enddate) {
		this.enddate = enddate;
	}


}
