package org.awsCognito.sdk;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

public class DeleteUser {
	public static void main(String [] args) {
		
		deleteUser("pavan1", "us-east-1_o6ha1hMHg");
	}

	public static Object deleteUser(String username, String userPoolId) {

		CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().region(Region.US_EAST_1)
				.build();
		
		try {
			AdminDeleteUserRequest request = AdminDeleteUserRequest.builder().username(username).userPoolId(userPoolId)
					.build();

			AdminDeleteUserResponse response = cognitoClient.adminDeleteUser(request);
			System.out.println("User " + response.toString() + " deleted. ID: " + request.username());

		} catch (CognitoIdentityProviderException e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
		cognitoClient.close();
		return null;
	}
}
