package org.awsCognito.sdk;
/*
 * This Class is used to Change the user password in cognito user pool.
 * 
 * author @pavan
 * 
 * user data can be changed from application.properties file from the classpath
 * 
 */


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordResponse;

public class ChangePassword {

	public static void main(String[] args) {

		String accessToken = "";
		String oldPassword = "Test@123";
		String newPassword = "test@123";

		changePassword(accessToken, oldPassword, newPassword);
	}

	public static void changePassword(String accessToken, String oldPassword, String newPassword) {

		try {
			CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
					.region(Region.US_EAST_1).build();

			ChangePasswordRequest request = ChangePasswordRequest.builder().accessToken(accessToken)
					.previousPassword(oldPassword).proposedPassword(newPassword).build();
			
			ChangePasswordResponse response = cognitoClient.changePassword(request);
			
			System.out.println(response);

		} catch (final Exception ex) {
			System.out.println("Exception" + ex);

		}
	}

}
