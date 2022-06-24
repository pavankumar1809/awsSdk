package org.awsCognito.sdk;

/*
 * This Class is used to create a user in aws cognito userpool.
 * 
 * author @pavan
 * 
 * user data can be changed from user-config.properties file
 * 
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDisableUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDisableUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

public class CreateUser {

//	Main class method.

	public static void main(String[] args) {

//		Call the create user function

		Object r = createUserOperation();
		System.out.println(r);
	}

	public static Object createUserOperation() {

		/*
		 * Declare empty variables for all the attributes.
		 */
		
		String userPoolId = "";
		String username = "";
		String email = "";
		String password = "";
		int status = 0;

		
		/*
		 * load a properties file from class path, inside static method
		 */
		Properties prop = new Properties();

		try (InputStream input = new FileInputStream(
				"C:\\Users\\GSC-30851\\Desktop\\test\\awsCognito\\src\\main\\resources\\user-config.properties")) {

			prop.load(input);

			/*
			 * get the property value and assign it to variables.
			 */
			
			userPoolId = prop.getProperty("userPoolId");
			username = prop.getProperty("username");
			email = prop.getProperty("email");
			password = prop.getProperty("password");
			status = Integer.parseInt(prop.getProperty("status"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		if (username.equals(null) || email.equals(null)) {
			return null;
		}
		
		/*
		 * Create cognito client instance
		 */
		
		CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().region(Region.US_EAST_1)
				.build();

		List<AttributeType> attributes = new ArrayList<AttributeType>();

		try {
			
			/*
			 * Required attribue
			 */
			attributes.add(AttributeType.builder().name("email").value(email).build());
			
			
			
			/*
			 * Optional attributes
			 * 
			 * attributes.add(AttributeType.builder().name("preferred_username").value(username).build());
			 * attributes.add(AttributeType.builder().name("given_name").value(user.
			 * getFirstname()).build());
			 * attributes.add(AttributeType.builder().name("family_name").value(user.
			 * getLastname()).build());
			 * attributes.add(AttributeType.builder().name("custom:title").value(user.
			 * getTitle()).build());
			 * attributes.add(AttributeType.builder().name("custom:department").value(user.
			 * getDepartment()).build());
			 * attributes.add(AttributeType.builder().name("custom:company").value(user.
			 * getCompany()).build());
			 * attributes.add(AttributeType.builder().name("phone_number").value(user.
			 * getPhone()).build());
			 * attributes.add(AttributeType.builder().name("custom:manager_id").value(user.
			 * getManager_id()).build());
			 */

			
			/* 
			 * create user Request and send request with cognito client. 
			 */
			
			
			AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder().userPoolId(userPoolId)
					.username(username).temporaryPassword(password).userAttributes(attributes).messageAction("SUPPRESS")
					.build();

			AdminCreateUserResponse response = cognitoClient.adminCreateUser(userRequest);
			
			/*
			 * Disable or enable the user as per status variable.
			 */
			if (status == 0) {
				AdminDisableUserRequest disable = AdminDisableUserRequest.builder().username(username)
						.userPoolId(userPoolId).build();
				AdminDisableUserResponse disableResponse = cognitoClient.adminDisableUser(disable);
				System.out.println(disableResponse);
			}

			System.out.println(response);
		} catch (CognitoIdentityProviderException e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
		cognitoClient.close();
		return null;
	}
}
