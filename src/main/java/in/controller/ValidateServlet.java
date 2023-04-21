package in.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ValidateServlet
 */
@WebServlet(
		urlPatterns = { "/test" }, 
		initParams = { 
				@WebInitParam(name = "jdbcURL", value = "jdbc:mysql://localhost:3306/localdb"), 
				@WebInitParam(name = "user", value = "root"), 
				@WebInitParam(name = "password", value = "mysql@123")
		})
public class ValidateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 Connection connection=null;
	 PreparedStatement pstmt=null;
	 ResultSet resultSet=null;

	@Override
	public void init() throws ServletException {
		ServletConfig config = getServletConfig();
		String url = config.getInitParameter("jdbcURL");
		String user = config.getInitParameter("user");
		String password = config.getInitParameter("password");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			  connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String name = request.getParameter("uname");
		String password = request.getParameter("upwd");
		
		String query="select username,password from credentials";
		if(connection!=null) {
			try {
				pstmt=connection.prepareStatement(query);
				
				if(pstmt!=null) {
				 resultSet = pstmt.executeQuery();
				 
				 if(resultSet.next()) {
					 String uname=resultSet.getString(1);
					String pass= resultSet.getString(2);
					
					if(name.equals(uname) && password.equals(pass)) {
						// inbox page
						ServletContext context = getServletContext();
						RequestDispatcher rd = context.getRequestDispatcher("/inbox.jsp");
						rd.forward(request, response);
					}
					else {
						// error page
						ServletContext context = getServletContext();
						RequestDispatcher rd = context.getRequestDispatcher("/error.jsp");
						rd.forward(request, response);
					}
				 }
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
