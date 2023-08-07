package in.globalit.service;

import java.util.List;

import in.globalit.binding.CaseWorkerCreationBinder;
import in.globalit.binding.DashboardResponseBinder;
import in.globalit.binding.LoginBinder;
import in.globalit.binding.PlanBinder;
import in.globalit.binding.UnlockAccountBinder;
import in.globalit.entity.PlanEntity;
import in.globalit.entity.UserEntity;

public interface AdminService {
	
	public String userLogin(LoginBinder login);
	public String caseWorkerRegistration(CaseWorkerCreationBinder signup);
	public Boolean createPlan(PlanBinder plan);
	public List<UserEntity> viewAllAccounts();
	public List<PlanEntity> viewAllPlans();
	public String unlockAccount(UnlockAccountBinder binder);
	
	public DashboardResponseBinder showDashboardRecords();
	public CaseWorkerCreationBinder editUserById(Long userId);
	public Boolean forgotPzzwd(String email);
	
	public PlanBinder editPlanById(Integer planId);
	public Boolean switchAccountActivateDeactivate(Long userId);
	public Boolean switchPlanActivateDeactivate(Integer planId);
	
	
	//edit plans and activate and deactivate cw account 
	// edit and update plans and activate/deactivate plans
	

}
