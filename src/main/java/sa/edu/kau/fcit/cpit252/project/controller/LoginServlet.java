package sa.edu.kau.fcit.cpit252.project.controller;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sa.edu.kau.fcit.cpit252.project.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private Login login;

    @Override
    public void init() throws ServletException {
        this.login = new Login();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = login.handleLoginAndGetUser(email, password);

        if (user != null) {
            // Store user information in session
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userName", user.getName());
            session.setAttribute("userEmail", user.getEmail());
            resp.sendRedirect("index.html");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h1>Invalid email or password</h1>");
        }
    }
}
