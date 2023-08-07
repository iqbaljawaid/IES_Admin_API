package in.globalit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.globalit.binding.CaseWorkerCreationBinder;
import in.globalit.binding.DashboardResponseBinder;
import in.globalit.binding.LoginBinder;
import in.globalit.binding.PlanBinder;
import in.globalit.binding.UnlockAccountBinder;
import in.globalit.constants.AppConstants;
import in.globalit.entity.PlanEntity;
import in.globalit.entity.RoleEntity;
import in.globalit.entity.UserEntity;
import in.globalit.props.AppProperties;
import in.globalit.repository.PlanRepo;
import in.globalit.repository.RoleRepo;
import in.globalit.repository.UserRepo;
import in.globalit.service.AdminService;
import in.globalit.utils.EmailSender;
import in.globalit.utils.GenerateOTP;

@Service
public class AdminServiceImpl implements AdminService {

	/*
	 * @Autowired public AdminServiceImpl(UserRepo userRepo, BCryptPasswordEncoder
	 * bcryptPzzwdEncoder, AppProperties appProps) { this.userRepo = userRepo;
	 * this.bcryptPzzwdEncoder = bcryptPzzwdEncoder; this.appProps = appProps; }
	 */

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder bcryptPzzwdEncoder;

	/*
	 * @Autowired private HttpSession session;
	 */
	@Autowired
	private GenerateOTP otp;

	@Autowired
	private EmailSender mailSender;

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private AppProperties appProps;

	/*
	 * private static final String SECRET_KEY = KeyGenerator.generateHexKey(32);
	 * private static final String SALT = "globalit";
	 */

	@Override
	public String userLogin(LoginBinder login) {
		UserEntity entity = userRepo.findByUserName(login.getUsername());

		if (entity != null && bcryptPzzwdEncoder.matches(login.getPzzwd(), entity.getPzzwd())) {
			if (entity.getRole().getAuthority().equals(AppConstants.ADMIN_STR)) {
				// session.setAttribute(AppConstants.USER_SESSION_ID, entity.getUserId());
				return AppConstants.SUCCESS_STR;
			} else {
				// session.setAttribute(AppConstants.USER_SESSION_ID, entity.getUserId());
				return appProps.getMessages().get(AppConstants.USER_LOGGED_IN_KEY);
			}
		} else {
			return appProps.getMessages().get(AppConstants.BAD_CREDENTIALS_KEY);
		}

	}

	@Override
	public String caseWorkerRegistration(CaseWorkerCreationBinder signup) {

		UserEntity userEntity = new UserEntity();
		RoleEntity roleEntity = new RoleEntity();
		BeanUtils.copyProperties(signup, userEntity);
		Long roleId = 1l;
		; // here we are hard coding it but in real time it will come from session
		String generateOTP = otp.generateOTP();

		// String encryptedPassword = Encryptors.text(SECRET_KEY,
		// SALT).encrypt(generateOTP);

		// String convertToHex = convertToHex(generateOTP);
		// userEntity.setPzzwd(bcryptPzzwdEncoder.encode(encryptedPassword));
		userEntity.setPzzwd(bcryptPzzwdEncoder.encode(generateOTP));
		userEntity.setAccStatus(AppConstants.LOCKED_STR);
		userEntity.setAccAction(AppConstants.DEACTIVATED_STR);
		roleEntity.setAuthority(AppConstants.USER_STR);
		roleEntity.setUsername(userEntity.getUsername());
		userEntity.setRole(roleEntity);
		String email = signup.getUsername();

		String to = email;
		String subject = appProps.getMessages().get(AppConstants.MAIL_SUBJECT_KEY);
		String body = "<body style='font-weight: bold;font-size: 15px'>"
				+ "<h2 style='font-family: 'Trocchi', serif; font-size: 45px; font-weight: normal; line-height: 48px; margin: 0;'>Your onetime password for registration is : "
				+ generateOTP + "</h2>" + "<br>" + "<" + AppConstants.H4_HEADER_MAIL_STR
				+ " style='font-family: 'Trocchi', serif; font-size: 15px; font-weight: normal; line-height: 18px; margin: 0;'> Please use this OTP to complete your new user registration."
				+ "</" + AppConstants.H4_HEADER_MAIL_STR + "<br>" + "<" + AppConstants.H4_HEADER_MAIL_STR
				+ "style='font-family: 'Trocchi', serif; font-size: 15px; font-weight: normal; line-height: 18px; margin: 0;'>To unlock your account, click this link."
				+ "<" + AppConstants.H4_HEADER_MAIL_STR + "/>" + "<a href='http://localhost:9091/unlock?email=" + to
				+ "'>Unlock</a>" + "</body>";

		// if (userEntity.getRole().getAuthority().equals(AppConstants.ADMIN_STR))
		Optional<RoleEntity> findById = roleRepo.findById(roleId);
		if (findById.isPresent()) {
			if (userEntity.getUserId() != null) {
				Optional<UserEntity> byId = userRepo.findById(userEntity.getUserId());
				{
					if (byId.isPresent()) {
						UserEntity save = userRepo.save(userEntity);
						if (save.getUserId() != null) {
							return appProps.getMessages().get(AppConstants.SUCCESSFULLY_EDITED);
						}
					}
				}
			}
			UserEntity save = userRepo.save(userEntity);
			if (save.getUserId() != null) {
				mailSender.sendEmail(to, subject, body);
				return AppConstants.SUCCESS_STR;
			} else {
				return appProps.getMessages().get(AppConstants.MAIL_NOT_SENT_KEY);
			}
		} else {
			return appProps.getMessages().get(AppConstants.ONLY_ADMIN_CREATES_ACCOUNT_KEY);
		}

	}

	/*
	 * private String convertToHex(String text) {
	 * 
	 * StringBuilder hexString = new StringBuilder();
	 * 
	 * for (char c : text.toCharArray()) { //
	 * hexString.append(Integer.toHexString((int) c)); String hex =
	 * Integer.toHexString((int) c); if (hex.length() % 2 != 0) { hex = "0" + hex; }
	 * // Pad with leading zero if the hex string has odd length }
	 * hexString.append(hex); } return hexString.toString(); }
	 */

	@Override
	public List<UserEntity> viewAllAccounts() {

		return userRepo.findAll();
	}

	@Override
	public Boolean createPlan(PlanBinder plan) {
		PlanEntity planEntity = new PlanEntity();

		BeanUtils.copyProperties(plan, planEntity);
		// Long userId = (Long) session.getAttribute(AppConstants.USER_SESSION_ID);
		Long userId = 1l; // here we are hard coding the id but we will take id from ui in form of session
							// when our ui is complete
		Optional<UserEntity> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserEntity userEntity = findById.get();
			planEntity.setUser(userEntity);
			planEntity.setPlanAction(AppConstants.ACTIVATED_STR);
		}
		if (planEntity.getUser().getRole().getAuthority().equals(AppConstants.ADMIN_STR)) {
			planRepo.save(planEntity);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<PlanEntity> viewAllPlans() {
		return planRepo.findAll();
	}

	@Override
	public String unlockAccount(UnlockAccountBinder binder) {
		UserEntity entity = userRepo.findByUserName(binder.getEmail());
		if (entity != null) {

			if (!bcryptPzzwdEncoder.matches(binder.getPzzwd(), entity.getPzzwd())) {
				return appProps.getMessages().get(AppConstants.BAD_CREDENTIALS_KEY);
			}
			if (AppConstants.UNLOCKED_STR.equals(entity.getAccStatus())) {
				return appProps.getMessages().get(AppConstants.ACCOUNT_ALREADY_UNLOCKED_KEY);

			}
			if (!binder.getNewPzzwd().equals(binder.getCnfPzzwd())) {
				return appProps.getMessages().get(AppConstants.PASSWORDS_DONT_MATCH_KEY);
			}
			entity.setPzzwd(bcryptPzzwdEncoder.encode(binder.getNewPzzwd()));
			entity.setAccStatus(AppConstants.UNLOCKED_STR);
			entity.setAccAction(AppConstants.ACTIVATED_STR);
			userRepo.save(entity);

			return AppConstants.SUCCESS_STR;
		}
		return appProps.getMessages().get(AppConstants.NO_RECORDS_KEY);

	}

	@Override
	public DashboardResponseBinder showDashboardRecords() {
		DashboardResponseBinder dashboard = new DashboardResponseBinder();
		List<PlanEntity> planList = planRepo.findAll();
		int totalPlans = planList.size();
		dashboard.setPlansAvailable(totalPlans);

		// remaining code for approved and denied citizens and benefits
		// will be implemented here

		return dashboard;
	}

	@Override
	public CaseWorkerCreationBinder editUserById(Long userId) {
		CaseWorkerCreationBinder cw = new CaseWorkerCreationBinder();
		// Long userId = (Long) session.getAttribute(AppConstants.USER_SESSION_ID);
		Optional<UserEntity> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserEntity userEntity = findById.get();
			BeanUtils.copyProperties(userEntity, cw);
		}

		return cw;
	}

	@Override
	public Boolean forgotPzzwd(String email) {
		// same logic as unlock page
		return false;
	}

	@Override
	public PlanBinder editPlanById(Integer planId) {
		PlanBinder planBinder = new PlanBinder();
		Optional<PlanEntity> findById = planRepo.findById(planId);
		if (findById.isPresent()) {
			PlanEntity planEntity = findById.get();
			BeanUtils.copyProperties(planEntity, planBinder);
		}
		return planBinder;
	}

	@Override
	public Boolean switchAccountActivateDeactivate(Long userId) {
		Optional<UserEntity> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserEntity userEntity = findById.get();
			if (userEntity.getAccAction().equals(AppConstants.ACTIVATED_STR)) {
				userEntity.setAccAction(AppConstants.DEACTIVATED_STR);
			} else {
				userEntity.setAccAction(AppConstants.ACTIVATED_STR);
			}
			userRepo.save(userEntity);
			return true;
		}
		return false;
	}

	@Override
	public Boolean switchPlanActivateDeactivate(Integer planId) {
		Optional<PlanEntity> findById = planRepo.findById(planId);
		if (findById.isPresent()) {
			PlanEntity planEntity = findById.get();
			if (planEntity.getPlanAction().equals(AppConstants.ACTIVATED_STR)) {
				planEntity.setPlanAction(AppConstants.DEACTIVATED_STR);
			} else {
				planEntity.setPlanAction(AppConstants.ACTIVATED_STR);
			}
			planRepo.save(planEntity);
			return true;
		}
		return false;
	}

}
