package in.globalit.binding;

import lombok.Data;

@Data
public class UnlockAccountBinder {

	private String email;
	private String pzzwd;
	private String newPzzwd;
	private String cnfPzzwd;
}
