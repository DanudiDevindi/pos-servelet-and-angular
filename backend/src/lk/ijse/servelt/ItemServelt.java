package lk.ijse.servelt;

import javax.annotation.Resource;
import javax.json.*;
import javax.json.stream.JsonParsingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/items")
public class ItemServelt  extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try (PrintWriter out = resp.getWriter()) {
//
//            if (req.getParameter("code") != null) {
//
//                String code = req.getParameter("code");
//
//                try {
//                    Connection connection = ds.getConnection();
//                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM item WHERE code=?");
//                    pstm.setObject(1, code);
//                    ResultSet rst = pstm.executeQuery();
//
//                    if (rst.next()) {
//                        JsonObjectBuilder ob = Json.createObjectBuilder();
//                        ob.add("code", rst.getString(1));
//                        ob.add("description", rst.getString(2));
//                        ob.add("unitPrice", rst.getString(3));
//                        ob.add("qtyOnHand", rst.getInt(4));
//                        resp.setContentType("application/json");
//                        out.println(ob.build());
//                    } else {
//                        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
//                    }
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//            } else {
//                try {
//
//                    Connection connection = ds.getConnection();
//                    Statement stm = connection.createStatement();
//                    ResultSet rst = stm.executeQuery("SELECT * FROM item");
//
//                    resp.setContentType("application/json");
//
//                    JsonArrayBuilder ab = Json.createArrayBuilder();
//
//                    while (rst.next()) {
//                        JsonObjectBuilder ob = Json.createObjectBuilder();
//                        ob.add("code", rst.getString(1));
//                        ob.add("description", rst.getString(2));
//                        ob.add("unitPrice", rst.getString(3));
//                        ob.add("qtyOnHand", rst.getInt(4));
//                        ab.add(ob.build());
//                    }
//                    out.println(ab.build());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//            }
//        }



        try (PrintWriter out = resp.getWriter()) {

            resp.setContentType("application/json");

            try {
                Connection connection = ds.getConnection();

                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Item");

                JsonArrayBuilder items = Json.createArrayBuilder();

                while (rst.next()){
                    String code = rst.getString("code");
                    String description = rst.getString("description");
                    String unitPrice = rst.getString("unitPrice");
                    int qtyOnHand = rst.getInt("qtyOnHand");


                    JsonObject item = Json.createObjectBuilder()
                            .add("code", code)
                            .add("description", description)
                            .add("unitPrice",unitPrice)
                            .add("qtyOnHand", qtyOnHand)
                            .build();
                    items.add(item);
                }

                out.println(items.build().toString());

                connection.close();
            } catch (Exception ex) {
                resp.sendError(500, ex.getMessage());
                ex.printStackTrace();
            }

        }


    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        Connection connection = null;

        try {
            JsonObject item = reader.readObject();
            String code = item.getString("code");
            String description = item.getString("description");
            String unitPrice = item.getString("unitPrice");
            Integer qtyOnHand = item.getInt("qtyOnHand");
            connection = ds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO item VALUES (?,?,?,?)");
            pstm.setObject(1, code);
            pstm.setObject(2, description);
            pstm.setObject(3, unitPrice);
            pstm.setObject(4, qtyOnHand);
            boolean result = pstm.executeUpdate() > 0;

            if (result) {
                out.println("true");
            } else {
                out.println("false");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("false");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");

        if (code!= null) {

            try {
                Connection connection = ds.getConnection();
                PreparedStatement pstm = connection.prepareStatement("DELETE FROM item WHERE code=?");
                pstm.setObject(1, code);
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                ex.printStackTrace();
            }

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter("code") != null){

            try {
                JsonReader reader = Json.createReader(req.getReader());
                JsonObject item = reader.readObject();
                System.out.println(req.getParameter("id"));
                String code = item.getString("code");
                String description = item.getString("description");
                String unitPrice = item.getString("unitPrice");
                Integer qtyOnHand = item.getInt("qtyOnHand");

                if (!code.equals(req.getParameter("code"))){
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                Connection connection = ds.getConnection();
                PreparedStatement pstm = connection.prepareStatement("UPDATE item SET description=?, unitPrice=?,qtyOnHand=?  WHERE code =?");
                pstm.setObject(4,code);
                pstm.setObject(3,qtyOnHand);
                pstm.setObject(2,unitPrice);
                pstm.setObject(1,description);
                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0){
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }else{
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

            }catch (JsonParsingException | NullPointerException  ex){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }catch (Exception ex){
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }


        }else{
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }





}

