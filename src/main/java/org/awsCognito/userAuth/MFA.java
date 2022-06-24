package org.awsCognito.userAuth;

import java.io.FileNotFoundException;

/*
 * This Class is used to perform MFA otp verification.
 * 
 * author @pavan
 * 
 * user data can be changed from application.properties file from the classpath
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.awsCognito.commons.OTP;
import org.awsCognito.commons.Path;
import org.awsCognito.commons.PropertiesCache;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

public class MFA {

	public static void main(String[] args) {

		userCustomAuthRequest();

	}

	public static RespondToAuthChallengeRequest userCustomAuthRequest() {

//		Create an instance of PropertyCache class to access properties file.
		Path path = new Path();
		String pathurl = path.getPath("application");
		PropertiesCache cache;
		try {
			cache = new PropertiesCache(pathurl);
//			Create cognito client to process API requests.

			CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
					.region(Region.US_EAST_1).build();

//	        Map all the parameters with the data from properties file.

			Map<String, String> challengeResponses = new HashMap<String, String>();
			challengeResponses.put("ANSWER", cache.getProperty("otp"));
			challengeResponses.put("USERNAME", cache.getProperty("username"));
			challengeResponses.put("SECRET_HASH", cache.getProperty("secretHash"));

			RespondToAuthChallengeRequest request = RespondToAuthChallengeRequest.builder()
					.challengeName(cache.getProperty("challengeName")).clientId(cache.getProperty("clientId"))
					.session(cache.getProperty("session")).challengeResponses(challengeResponses).build();

//			send the API request to aws cognito for CUSTOM_CHALLENGE.

			RespondToAuthChallengeResponse result = cognitoClient.respondToAuthChallenge(request);
			System.out.println(result);

			try {

//				Fetch the access token if authentication is successful.
				String token = result.authenticationResult().accessToken();
//				AccessToken.setAccessToken(token);
				System.out.println(token);
			}

			catch (NullPointerException e) {

//				Updating session data and verification code if otp is invalid.
				System.out.println(e);
				cache.setProperty("session", result.session());
				OTP.getOTP(result.challengeParameters().get("verificationCode"));
				System.out.println("Invalid OTP");
			}

//			After maximum tries returns authentication failure...
			catch (NotAuthorizedException e) {
				System.out.println(e);
			}
			return request;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
	}

}
