package in.globalit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import in.globalit.binding.CaseWorkerCreationBinder;
import in.globalit.binding.DashboardResponseBinder;
import in.globalit.binding.LoginBinder;
import in.globalit.binding.PlanBinder;
import in.globalit.binding.UnlockAccountBinder;
import in.globalit.constants.AppConstants;
import in.globalit.entity.PlanEntity;
import in.globalit.entity.UserEntity;
import in.globalit.props.AppProperties;
import in.globalit.service.AdminService;

@RestController
public class AdminRestController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AppProperties appProps;
	
	@PostMapping ("/loginCheck")
	public ResponseEntity<String> appLogin(@RequestBody LoginBinder login){
		String status = adminService.userLogin(login);
		if(status.contains(AppConstants.SUCCESS_STR)) {
			return new ResponseEntity<>(appProps.getMessages().get(AppConstants.ADMIN_LOGGED_IN_KEY),HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(status,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> caseWorkerSignup(@RequestBody CaseWorkerCreationBinder signup,
									@RequestHeader("User-Id") String authorizationHeader){
		
		
		String status = adminService.caseWorkerRegistration(signup);
		if(status.contains(AppConstants.SUCCESS_STR)) {
			return new ResponseEntity<>(appProps.getMessages().get(AppConstants.MAIL_SENT_KEY),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(status,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/editUser/{userId}")
	public CaseWorkerCreationBinder editUser(@PathVariable Long userId,
			@RequestHeader("User-Id") String authorizationHeader) {
		return adminService.editUserById(userId);
	}
	
	/*
	 * @GetMapping("/editUser/{userId}") public ResponseEntity<String>
	 * editUser(@PathVariable Long userId,
	 * 
	 * @RequestHeader("User-Id") String authorizationHeader) {
	 * 
	 * 
	 * CaseWorkerCreationBinder editUserById = adminService.editUserById(userId);
	 * return caseWorkerSignup(editUserById, authorizationHeader); }
	 */
	
	@PostMapping("/unlock")
	public ResponseEntity<String> unlockYourAccount(@RequestBody UnlockAccountBinder unlock){
		String accountStatus = adminService.unlockAccount(unlock);
		if(accountStatus.contains(AppConstants.SUCCESS_STR)) {
			return new ResponseEntity<>(appProps.getMessages().get(AppConstants.ACCOUNT_UNLOCKED_SUCCESS_KEY),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(accountStatus,HttpStatus.OK);
		}
	}
	
	@PostMapping("/createPlan")
	public ResponseEntity<String> createYourPlan(@RequestBody PlanBinder plan){
		Boolean createPlan = adminService.createPlan(plan);
		if(Boolean.TRUE.equals(createPlan)) {
			return new ResponseEntity<>(appProps.getMessages().get(AppConstants.PLAN_CREATED_KEY),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(appProps.getMessages().get(AppConstants.PLAN_NOT_CREATED_KEY),HttpStatus.OK);
		}
	}
	
	@GetMapping("/editPlan/{planId}")
	public PlanBinder editUser(@PathVariable Integer planId,
			@RequestHeader("User-Id") String authorizationHeader) {
		return adminService.editPlanById(planId);
	}
	
	@GetMapping(value = "/viewUsers", produces = "application/json")
	public List<UserEntity> viewAllUsers(){
		return adminService.viewAllAccounts();
	}
	
	@GetMapping("/viewPlans")
	public List<PlanEntity> viewAllPlans(){
		return adminService.viewAllPlans();
	}
	
	@GetMapping("/switchUser/{userId}")
	public String switchAccountAction(@PathVariable Long userId) {
		Boolean activateDeactivate = adminService.switchAccountActivateDeactivate(userId);
		if(Boolean.TRUE.equals(activateDeactivate)) {
			return appProps.getMessages().get(AppConstants.ACCOUNT_SWITCH_SUCCESS_KEY);
		}else {
			return appProps.getMessages().get(AppConstants.ACCOUNT_SWITCH_FAILURE_KEY);
		}
	}
	
	@GetMapping("/switchPlan/{planId}")
	public String switchPlanAction(@PathVariable Integer planId) {
		Boolean activateDeactivate = adminService.switchPlanActivateDeactivate(planId);
		if(Boolean.TRUE.equals(activateDeactivate)) {
			return appProps.getMessages().get(AppConstants.PLAN_SWITCH_SUCCESS_KEY);
		}else {
			return appProps.getMessages().get(AppConstants.PLAN_SWITCH_FAILURE_KEY);
		}
	}
	
	@GetMapping("/viewDashboard")
	public DashboardResponseBinder viewDashboardRecords() {
		return adminService.showDashboardRecords();
		
	}
	
	
	
	
	

}
