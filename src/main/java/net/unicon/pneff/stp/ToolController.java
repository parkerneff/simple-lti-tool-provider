package net.unicon.pneff.stp;

import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsglobal.lti.launch.LtiVerifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ToolController {

    @PostMapping("/greeting")
    public String greeting(HttpServletRequest request,
                           @RequestParam(name="oauth_consumer_key", required=true) String consumerKey,
                           @RequestParam(name="launch_presentation_return_url", required = false) String returnUrl,
                           @RequestParam(name="lis_person_name_full", required = false, defaultValue = "Unknown") String fullName,
                           Model model) {
        dumpRequest(request);
        System.out.println("Consumer key = " + consumerKey);
        String secret = "secret";
        System.out.println("faked a lookup of consumer key and got secret=" + secret);
        boolean verified = false;

        LtiVerifier verifier = new LtiOauthVerifier();


        try {
            LtiVerificationResult result = verifier.verify(request, secret);
            verified = result.getSuccess();
            System.out.println("Verified: " + verified);
            if (!verified) {
                System.err.println("Validation error=" + result.getMessage());
                System.err.println("LTI error=" + result.getError().toString());
            }
           // System.out.println("Error: " + result.getError().toString());
        } catch (LtiVerificationException e) {
            e.printStackTrace();
        }

        model.addAttribute("verified", verified);
        model.addAttribute("name", fullName);
        model.addAttribute("returnUrl", returnUrl);
        return "tool-welcome";
    }
    private void dumpRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("--- Request Data ---");
        for (String name : request.getParameterMap().keySet()) {
            sb.append("\n\t" + name + ": " + request.getParameter(name));

        }
        System.out.println(sb.toString());
    }

}
