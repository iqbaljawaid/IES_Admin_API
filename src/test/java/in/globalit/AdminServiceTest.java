package in.globalit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import in.globalit.binding.LoginBinder;
import in.globalit.constants.AppConstants;
import in.globalit.entity.RoleEntity;
import in.globalit.entity.UserEntity;
import in.globalit.props.AppProperties;
import in.globalit.repository.UserRepo;
import in.globalit.service.impl.AdminServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
	
	@MockBean
	private UserRepo userRepo;
	
	@MockBean
	private BCryptPasswordEncoder bcryptPzzwdEncoder;
	
	@MockBean
	private AppProperties appProps;
	
	@InjectMocks
	private AdminServiceImpl adminService;

	@Test
	public void testUserLogin_Admin_Success() {
		// Create a mock objects for UserEntity with admin role,Bcrypt encryption and
		// AppProperties
		/*
		 * UserRepo userRepo = mock(UserRepo.class); BCryptPasswordEncoder
		 * bcryptPzzwdEncoder = mock(BCryptPasswordEncoder.class); AppProperties
		 * appProps = mock(AppProperties.class); System.out.println(userRepo);
		 * write this when you have constructor injection in your service class also else use @MockBean and 
		 * AdminServiceImpl adminService = new AdminServiceImpl(userRepo,bcryptPzzwdEncoder, appProps);
		 */
		// Create a mock RoleEntity with authority "ADMIN"
		RoleEntity adminRole = mock(RoleEntity.class);
		//RoleEntity adminRole = new RoleEntity();
		
		when(adminRole.getAuthority()).thenReturn(AppConstants.ADMIN_STR);

		// Create a mock UserEntity with the admin role
		//UserEntity adminUser = mock(UserEntity.class);
		UserEntity adminUser = new UserEntity();
		adminUser.setRole(adminRole);
		adminUser.setPzzwd("password");

		// Verify that findByUsername is called with "admin" username
		//UserEntity userEntity = verify(userRepo).findByUserName("ADMIN");
		//System.out.println(userEntity);

		// Mock the UserRepository to return the admin user when findByUsername is
		// called
		when(userRepo.findByUserName(anyString())).thenReturn(adminUser);

		// Mock the bcryptPzzwdEncoder to always return true
		when(bcryptPzzwdEncoder.matches(anyString(), anyString())).thenReturn(true);

		// Invoke the userLogin method with valid admin credentials
		LoginBinder login = new LoginBinder();
		login.setUsername("admin");
		login.setPzzwd("password");
		String result = adminService.userLogin(login);

		// Print additional debug information
		System.out.println("Result: " + result);

		// Verify that the expected result is returned
		assertEquals(AppConstants.SUCCESS_STR, result);

	}
	
	@Test
	public void testUserLogin_User_Success() {
		
		// Create a mock RoleEntity with authority "USER"
		RoleEntity userRole = mock(RoleEntity.class);
		when(userRole.getAuthority()).thenReturn(AppConstants.USER_STR);

		// Create a mock UserEntity with the user role
		//UserEntity adminUser = mock(UserEntity.class);
		UserEntity adminUser = new UserEntity();
		adminUser.setRole(userRole);
		adminUser.setPzzwd("password");

		// Verify that findByUsername is called with "admin" username
		//UserEntity userEntity = verify(userRepo).findByUserName("ADMIN");
		//System.out.println(userEntity);

		// Mock the UserRepository to return the user when findByUsername is
		// called
		when(userRepo.findByUserName(anyString())).thenReturn(adminUser);

		// Mock the bcryptPzzwdEncoder to always return true
		when(bcryptPzzwdEncoder.matches(anyString(), anyString())).thenReturn(true);

		// Invoke the userLogin method with valid admin credentials
		LoginBinder login = new LoginBinder();
		login.setUsername("admin");
		login.setPzzwd("password");
		String result = adminService.userLogin(login);

		// Print additional debug information
		System.out.println("Result: " + result);

		// Verify that the expected result is returned
		assertEquals(appProps.getMessages().get(AppConstants.USER_LOGGED_IN_KEY), result);

	}


}
