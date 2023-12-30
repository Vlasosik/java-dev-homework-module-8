package org.example.crud;

import org.example.entity.Client;
import org.example.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final String INSERT_INTO = "INSERT INTO client (NAME) VALUES (?)";
    private static final String SELECT_ID = "SELECT name FROM client WHERE id = ?";
    private static final String UPDATE_CLIENT = "UPDATE client SET name = ? WHERE id = ?";

    private static final String DELETE_CLIENT = "DELETE FROM client WHERE id = ?";

    private static final String SELECT_ALL_CLIENTS = "SELECT id, name FROM client";


    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        clientService.create("Pavel");
        clientService.getById(5L);
        clientService.setName(40L, "Misha");
        clientService.deleteById(32L);
        clientService.printClient();

    }

    long create(String name) {
        if (name.length() < 2 || name.length() > 1000) {
            throw new IllegalArgumentException("Invalid client name");
        }
        long clientId = -1;
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                clientId = resultSet.getLong(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return clientId;
    }

    public String getById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid client id");
        }
        String name = null;
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("NAME");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return name;
    }

    void setName(long id, String name) {
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);
            int rowsUpdate = preparedStatement.executeUpdate();
            if (rowsUpdate > 0) {
                System.out.println("Name updated successfully");
            } else {
                throw new IllegalArgumentException("No client found with the given id: " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
    }

    void deleteById(long id) {
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT)) {
            preparedStatement.setLong(1, id);
            int rowsDelete = preparedStatement.executeUpdate();
            if (rowsDelete > 0) {
                System.out.println("Client with ID " + id + " deleted successfully");
            } else {
                throw new IllegalArgumentException("Not been deleted by id: " + id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
    }

    List<Client> listAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getLong("id"));
                client.setName(resultSet.getString("name"));
                clients.add(client);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return clients;
    }

    private void printClient() {
        List<Client> clients = listAll();
        for (Client client : clients) {
            System.out.println("id: " + client.getId());
            System.out.println("name: " + client.getName());
        }
    }
}
