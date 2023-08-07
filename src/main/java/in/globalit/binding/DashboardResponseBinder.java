package in.globalit.binding;

import lombok.Data;

@Data
public class DashboardResponseBinder {
	
	private Integer plansAvailable;
	private Long citizensApproved;
	private Long citizensDenied;
	private Long benefitsGiven;

}
