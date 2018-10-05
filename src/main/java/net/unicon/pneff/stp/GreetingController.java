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
public class GreetingController {

    @PostMapping("/greeting")
    public String greeting(HttpServletRequest request, @RequestParam(name="name", required=false, defaultValue="World") String name,
                           @RequestParam(name="oauth_consumer_key", required=true) String consumerKey, Model model) {
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
           // System.out.println("Error: " + result.getError().toString());
        } catch (LtiVerificationException e) {
            e.printStackTrace();
        }

        model.addAttribute("verified", verified);
        model.addAttribute("name", name);
        return "greeting";
    }
    private void dumpRequest(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("--- Request Data ---");
        for (String name : request.getParameterMap().keySet()) {
            sb.append("\n\t" + name + ": " + request.getParameter(name));

        }
        System.out.println(sb.toString());
    }

}
