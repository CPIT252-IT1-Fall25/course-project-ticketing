package sa.edu.kau.fcit.cpit252.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/userinfo")
public class UserInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("userName");
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        PrintWriter out = resp.getWriter();
        
        if (userName != null) {
            // Escape quotes and backslashes in user name for JSON
            String escapedUserName = userName.replace("\\", "\\\\").replace("\"", "\\\"");
            out.print("{\"loggedIn\": true, \"userName\": \"" + escapedUserName + "\"}");
        } else {
            out.print("{\"loggedIn\": false}");
        }
        
        out.flush();
    }
}

