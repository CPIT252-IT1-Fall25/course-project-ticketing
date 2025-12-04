package sa.edu.kau.fcit.cpit252.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private Signup signup;

    @Override
    public void init() throws ServletException {
        this.signup = new Signup();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("Name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        boolean ok = signup.handleSignup(name, email, password);

        if (ok) {
            // Store user information in session after signup
            HttpSession session = req.getSession();
            User user = InMemoryUserStore.getInstance().findByEmail(email);
            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("userName", user.getName());
                session.setAttribute("userEmail", user.getEmail());
            }
            resp.sendRedirect("index.html");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h1>User already exists</h1>");
        }
    }
}
