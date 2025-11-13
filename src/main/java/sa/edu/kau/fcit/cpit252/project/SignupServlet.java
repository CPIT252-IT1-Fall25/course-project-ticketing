package sa.edu.kau.fcit.cpit252.project;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private Signup signup;

    @Override
    public void init() throws ServletException {
        // Signup internally uses singleton InMemoryUserStore
        this.signup = new Signup();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ⚠ Your HTML uses name="Name" (capital N)
        String name = req.getParameter("Name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        boolean ok = signup.handleSignup(name, email, password);

        if (ok) {
            // redirect to login page
            resp.sendRedirect("LogginPage.html");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<h1>User already exists</h1>");
        }
    }
}
