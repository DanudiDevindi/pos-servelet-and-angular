package lk.ijse.servelt;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/customers")
public class CustomerServelt extends HttpServlet {


        @Resource(name = "java:comp/env/jdbc/pool")
        private DataSource ds;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            try {

                Connection connection = ds.getConnection();
                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

                JsonArrayBuilder ab = Json.createArrayBuilder();
                while (rst.next()) {
                    JsonObjectBuilder ob = Json.createObjectBuilder();
                    ob.add("id", rst.getString("id"));
                    ob.add("name", rst.getString("name"));
                    ob.add("address", rst.getString("address"));

                    ab.add(ob.build());
                }
                JsonArray customers = ab.build();
                resp.setContentType("application/json");
                resp.getWriter().println(customers);

                connection.close();
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        Connection connection = null;

        try {
            JsonObject customer = reader.readObject();
            String id = customer.getString("id");
            String name = customer.getString("name");
            String address = customer.getString("address");
            connection = ds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?)");
            pstm.setObject(1, id);
            pstm.setObject(2, name);
            pstm.setObject(3, address);
            boolean result = pstm.executeUpdate()>0;

            if (result){
                out.println("true");
            }else{
                out.println("false");
            }

        }catch (Exception ex){
            ex.printStackTrace();
            out.println("false");
        }finally {
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            out.close();
        }
    }

    }


