package org.awsCognito.sdk;

import org.awsCognito.commons.SecretHash;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmForgotPasswordResponse;

public class ConfirmForgotPassword {

	public static void main(String[] args) {

		String clientId = "7enm2na5flqa00p3eicpjma8k3";
		String secretKey = "aldd94upn7698g8i4e62i1csvkssuj17c1r0sv27j556jijspbh";
		String username = "pavan";
		
		confirmForgotPassword(clientId, secretKey, username);
	}

	public static void confirmForgotPassword(String clientId, String secretKey, String username) {

		try {
			CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
					.region(Region.US_EAST_1).build();

			String confirmationCode = "936818";
			SecretHash secretHash = new SecretHash();
			ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder().clientId(clientId)
					.secretHash(secretHash.calculateSecretHash(clientId, secretKey, username)).username(username)
					.password("Test@123").confirmationCode(confirmationCode).build();

			ConfirmForgotPasswordResponse response = cognitoClient.confirmForgotPassword(request);

			System.out.println(response);
		} catch (final Exception ex) {
			System.out.println("Exception" + ex);

		}
	}
}
