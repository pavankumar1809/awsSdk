package org.awsCognito.sdk;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.RespondToAuthChallengeResult;

import org.awsCognito.commons.AuthenticationHelper;


public class CustomAuth {
	
	public static void main(String [] args) {
		
		String userPoolId = "us-east-1_o6ha1hMHg";
		String clientId = "6rqfo3vd9tqlj6g8epos6kfbfh";
		String secretKey = "aldd94upn7698g8i4e62i1csvkssuj17c1r0sv27j556jijspbh";
		String username = "pavan1";
		String password = "test@123";
		
		customAuthentication(userPoolId, clientId, null, username, password);
	}
	
	public static void customAuthentication(String userPoolId, String clientId, String secretKey, String username, String password) {
		
		try {
		AnonymousAWSCredentials awsCreds = new AnonymousAWSCredentials();
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.fromName("us-east-1"))
                .build();
        AuthenticationHelper authHelper = new AuthenticationHelper(userPoolId, clientId, secretKey);
		InitiateAuthRequest authRequest = initiateUserAuthRequest(username, clientId, secretKey, authHelper);
		
		InitiateAuthResult result = cognitoIdentityProvider.initiateAuth(authRequest);
		System.out.println(result);
		
		System.out.println(authRequest.getAuthParameters().get("SECRET_HASH"));
		
		RespondToAuthChallengeRequest request = authHelper.userSrpAuthRequest(result, password, authRequest.getAuthParameters().get("SECRET_HASH"));
		RespondToAuthChallengeResult authResult = cognitoIdentityProvider.respondToAuthChallenge(request);
		
		System.out.println(authResult);
		
		}catch (final Exception ex) {
            System.out.println("Exception" + ex);

        }
	}
	
	private static InitiateAuthRequest initiateUserAuthRequest(String username, String clientId, String secretKey, AuthenticationHelper authHelper) {

		
        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest();
        initiateAuthRequest.setAuthFlow(AuthFlowType.CUSTOM_AUTH);
        initiateAuthRequest.setClientId(clientId);
        //Only to be used if the pool contains the secret key.
        if (secretKey != null && !secretKey.isEmpty()) {
            initiateAuthRequest.addAuthParametersEntry("SECRET_HASH", authHelper.calculateSecretHash(clientId, secretKey, username));
        }
        initiateAuthRequest.addAuthParametersEntry("USERNAME", username);
        initiateAuthRequest.addAuthParametersEntry("CHALLENGE_NAME", "SRP_A");
        initiateAuthRequest.addAuthParametersEntry("SRP_A", authHelper.getA().toString(16));
        return initiateAuthRequest;
    }
	
	
}
