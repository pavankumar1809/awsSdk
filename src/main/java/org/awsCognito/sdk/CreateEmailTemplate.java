package org.awsCognito.sdk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.CreateEmailTemplateRequest;
import software.amazon.awssdk.services.sesv2.model.CreateEmailTemplateResponse;
import software.amazon.awssdk.services.sesv2.model.EmailTemplateContent;

public class CreateEmailTemplate {

	public static void main(String[] args) {

		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"C:\\Users\\GSC-30851\\Desktop\\Usher-Identity-service\\ushur-identity-service\\adaptors\\awsCognito\\src\\main\\java\\org\\awsCognito\\sdk\\emailTemplate.html"));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		String html = contentBuilder.toString();

		String templateName = "EmailVerification";

		createEmailtemplate(templateName, html);
	}

	public static void createEmailtemplate(String templateName, String html) {

		SesV2Client sesClient = SesV2Client.builder().region(Region.US_EAST_1).build();
		EmailTemplateContent templateContent = EmailTemplateContent.builder().subject("Confirmation Code").html(html)
				.build();

		CreateEmailTemplateRequest request = CreateEmailTemplateRequest.builder().templateName(templateName)
				.templateContent(templateContent).build();
		
		CreateEmailTemplateResponse response = sesClient.createEmailTemplate(request);
		
		System.out.println(response);
	}

}
