package org.spend.servlets;

import java.io.IOException;
import java.security.InvalidParameterException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.spend.manager.UserManager;
import org.spend.model.User;
import org.spend.persistence.DatabaseManager;


public class AccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PASSWORD = "password";

    private static final String LOGIN_ACTION = "login";
    private static final String LOGOUT_ACTION = "logout";
    private static final String REGISTER_ACTION = "register";
    public static final String LOGIN_HTML = "/shopping/login.html";
    public static final String SHOPPING_CATALOG_HTML = "/shopping/catalog.html";


    private UserManager userManager;
    private DatabaseManager db;

    @Override
    public void init() throws ServletException {
        super.init();
        this.db = new DatabaseManager();
        this.userManager = new UserManager(db.getUsers());
    }

    @Override
    public void destroy() {
        db.writeUsers(userManager.getUsers());
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            switch (action) {
                case LOGIN_ACTION:
                    login(request, response);
                    break;
                case LOGOUT_ACTION:
                    logout(request, response);
                    break;
                case REGISTER_ACTION:
                    register(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    break;
            }
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter(EMAIL);
        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String password = request.getParameter(PASSWORD);

        HttpSession session = request.getSession(false);
        if (!validateRegisterSession(session)) {
            String errorResponse = "You are already signed in. Please log out and try registering again.";
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorResponse);
            return;
        }

        try {
            User registeringUser = createUser(firstName, lastName, email, password);
            userManager.register(registeringUser);
            response.sendRedirect(LOGIN_HTML);
        } catch (InvalidParameterException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);

        HttpSession session = request.getSession(false);
        validateLoginSession(response, session, email);

        try {
            User userToLogin = createUser(email, password);
            User loggedInUser = userManager.loginUser(userToLogin);
            session = request.getSession(true);
            session.setAttribute(EMAIL, loggedInUser.getEmail());
            response.sendRedirect(SHOPPING_CATALOG_HTML);
        } catch (InvalidParameterException ex) {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            }
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(LOGIN_HTML);

    }

    private void validateLoginSession(HttpServletResponse response, HttpSession session, String email)
        throws IOException {
        if (session != null) {
            try {
                String sessionEmail = session.getAttribute(EMAIL).toString();
                if (email.equals(sessionEmail)) {
                    response.sendRedirect(SHOPPING_CATALOG_HTML);
                } else {
                    response.sendRedirect(LOGIN_HTML);
                }
                return;
            } catch (NullPointerException e) {
                session.invalidate();
                response.sendRedirect(LOGIN_HTML);
                return;
            }
        }
    }

    private boolean validateRegisterSession(HttpSession session) {
        if (session != null) {
            session.invalidate();
            return false;
        }
        return true;
    }

    private User createUser(String email, String password) {
        return User.builder()
            .email(email)
            .password(password)
            .build();
    }

    private User createUser(String firstName, String lastName, String email, String password) {
        return createUser(email, password).toBuilder()
            .firstName(firstName)
            .lastName(lastName)
            .build();
    }

}
