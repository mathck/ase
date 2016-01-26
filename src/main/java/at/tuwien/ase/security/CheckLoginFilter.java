package at.tuwien.ase.security;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.tuwien.ase.model.JsonStringWrapper;
import at.tuwien.ase.services.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Created by DanielHofer on 26.11.2015.
 */

@Controller
public class CheckLoginFilter extends GenericFilterBean {

    @Autowired
    private LoginService loginService;

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String userToken;

        String uri = ((HttpServletRequest) request).getRequestURI().toString();

        // todo use a whitelist instead of multiple if's
        //for register and for login the token validation is not necessary
        if (!uri.equalsIgnoreCase("/taskit/api/user/login")
                && !uri.equalsIgnoreCase("/taskit/api/user/register")
                    && !uri.contains("/generate/5796e83c-f5fa-4730-9915-a47bfcecad6d")) {

            //get user-token from request header
            userToken = httpRequest.getHeader("user-token");

            if ((userToken == null) || (userToken.equals(""))) {
                //auth token not found in http header
                createJsonError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, new String("Unauthorized: Authentication token missing."));
            } else {

                try {
                    //check validity of token in request header
                    if (!loginService.checkLogin(userToken)) {
                        //http token in request header is invalid
                        createJsonError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, new String("Unauthorized: Authentication token invalid."));
                    } else {
                        //http token in request header is valid
                        chain.doFilter(request, response);
                    }

                } catch (Exception e) {
                    createJsonError(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new String("Internal Error"));
                }

            }

        } else {
            //no token validation necessary
            chain.doFilter(request, response);
        }
    }

    private void createJsonError(HttpServletResponse response, int responseCode, String errorMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        //create jsonStringMapper object with error message
        JsonStringWrapper jsonStringWrapper = new JsonStringWrapper(errorMessage);
        //serialize object to json string
        String jsonString = mapper.writeValueAsString(jsonStringWrapper);

        response.setStatus(responseCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
    }
}
