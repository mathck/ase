package at.tuwien.ase.security;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.tuwien.ase.services.LoginService;
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

        String uri = ((HttpServletRequest)request).getRequestURI().toString();


        //for register and for login the token validation is not necessary
        if (!uri.equalsIgnoreCase("/taskit/api/user/login") && !uri.equalsIgnoreCase("/taskit/api/user/register")){

            //get user-token from request header
            userToken = httpRequest.getHeader("user-token");

            if ((userToken == null) || (userToken.equals(""))){
                //auth token not found in http header
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,   "Unauthorized: Authentication token missing." );
            }else {

                try {
                    //check validity of token in request header
                    if (!loginService.checkLogin(userToken)) {
                        //http token in request header is invalid
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Authentication token invalid.");
                    } else {
                        //http token in request header is valid
                        chain.doFilter(request, response);
                    }

                } catch (Exception e) {
                    httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Error");
                }

            }

        }else{
            //no token validation necessary
            chain.doFilter(request, response);
        }


    }
}
