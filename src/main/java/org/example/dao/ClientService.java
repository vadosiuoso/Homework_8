package org.example.dao;

import org.example.db.Database;
import org.example.model.Client;

import javax.sql.RowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final Connection connection;

    public ClientService() {
        connection = Database.getInstance().getConnection();
    }

    //Чи є якийсь інший спосіб повернути айді без додаткового запиту?
    public long create(String name) {
        validateName(name);
        String sql = "INSERT INTO client (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "SELECT id FROM client WHERE name = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(resultSet.isLast()){
                    return resultSet.getLong("id");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return -1;
    }

    private void validateName(String name){
        if(name.isEmpty() || name.length() < 2){
            throw new IllegalArgumentException("Name is empty or short");
        }
    }

    public void setName(long id, String name) {
        validateName(name);
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE clients SET name = ? WHERE id = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM clients WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Client with this id not available");
        }

        return null;
    }

    public void deleteById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clients WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Client with this id not available");
        }
    }

    public List<Client> listAll() {
        List<Client> clients = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM clients")) {

            while (resultSet.next()) {
                long clientId = resultSet.getLong("id");
                String clientName = resultSet.getString("name");
                clients.add(new Client(clientId, clientName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }
}

