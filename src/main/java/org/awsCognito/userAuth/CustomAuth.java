package org.awsCognito.userAuth;

/*
 * This Class is used to Authenticate a user in aws cognito userpool with email MFA using Custom Auth Lambda Triggers.
 * 
 * author @pavan
 * 
 * user data can be changed from application.properties file from the classpath
 * 
 */

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.awsCognito.commons.OTP;
import org.awsCognito.commons.Path;
import org.awsCognito.commons.PropertiesCache;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.RespondToAuthChallengeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.RespondToAuthChallengeResponse;

public class CustomAuth {

	public static void main(String[] args) {

		String userPoolId = "";
		String username = "";
		String clientId = "";
		String password = "";
		String secret = "";

		/*
		 * load a properties file from class path, inside static method
		 */

		String path = new Path().getPath("application");

		PropertiesCache cache;
		try {
			cache = new PropertiesCache(path);
			userPoolId = cache.getProperty("userPoolId");
			username = cache.getProperty("username");
			clientId = cache.getProperty("clientId");
			password = cache.getProperty("password");
			secret = cache.getProperty("clientSecret");

//			use AuthenticationHelper to complete SRP_A and PASSWORD_VERIFIER authenticaton.

			AuthenticationHelper authhelper = new AuthenticationHelper(userPoolId, clientId, secret);

			/*
			 * A challenge is returned from AuthenticationHelper, it contains all the
			 * details we need for MFA.
			 * 
			 * If some details are missing we can add them to the challenge.
			 */

			RespondToAuthChallengeResponse challenge = authhelper.PerformSRPAuthentication(username, password);

			String secretHash = null;
			try {
				secretHash = challenge.challengeParameters().get("SECRET_HASH");
			}
			catch(Exception e) {
				
			}

//			Add the following attributes required for MFA to properties file i class path.
			cache.setProperty("session", challenge.session());
			cache.setProperty("challengeName", challenge.challengeName().toString());
			cache.setProperty("secretHash", secretHash);

			OTP.getOTP(challenge.challengeParameters().get("verificationCode"));

			// Write to the file

			System.out.println(challenge);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		/*
		 * assign values to declared variables from application.properties file.
		 * 
		 * We can also fetch the data through APIs when using project.
		 * 
		 */

//		userCustomAuthRequest(challenge, clientId);

	}

	public static RespondToAuthChallengeRequest userCustomAuthRequest(RespondToAuthChallengeResponse challenge,
			String clientId) {

		String code = OTP.getOTP(challenge.challengeParameters().get("verificationCode"));

		CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().region(Region.US_EAST_1)
				.build();
		Map<String, String> challengeResponses = new HashMap<String, String>();

		challengeResponses.put("ANSWER", code);
		challengeResponses.put("USERNAME", "pavan");
		challengeResponses.put("SECRET_HASH", challenge.challengeParameters().get("SECRET_HASH"));

		RespondToAuthChallengeRequest request = RespondToAuthChallengeRequest.builder()
				.challengeName(challenge.challengeName()).clientId(challenge.challengeParameters().get("clientId"))
				.session(challenge.session()).build();

		RespondToAuthChallengeResponse response = cognitoClient.respondToAuthChallenge(request);

		System.out.println(response.authenticationResult().accessToken());
		return request;
	}
}
