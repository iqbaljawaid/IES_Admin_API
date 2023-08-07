package in.globalit.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CaseWorkerCreationBinder {

	private Long userId;
	private String fullName;
	private String username;
	private Long mobileNo;
	private String gender;
	private LocalDate dob;
	private Long ssn;
}
