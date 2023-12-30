package org.example.dao;

import org.example.module_structure_sql_querry.LongestProject;
import org.example.module_structure_sql_querry.ProjectPrices;
import org.example.module_structure_sql_querry.YoungestOldestWorkers;
import org.example.util.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseQueryService {
    public static void main(String[] args) {
        DatabaseQueryService databaseQueryService = new DatabaseQueryService();
        databaseQueryService.printsFindingTheLongestProject();
        databaseQueryService.printsFindClientWithMaxProject();
        databaseQueryService.printsFindAnWorkersWithTheMaximumSalary();
        databaseQueryService.printsFindTheYoungestAndOldestWorkers();
        databaseQueryService.printsFindsPricesForProjects();

    }

    public void printsFindingTheLongestProject() {
        List<LongestProject> longestProjects = findingTheLongestProject();
        for (LongestProject project : longestProjects) {
            System.out.println("projectID: " + project.getId());
            System.out.println("start Month: " + project.getStartMonth());
            System.out.println("finish Month: " + project.getFinishMonth());
            System.out.println("---------------------|");
        }
    }

    public void printsFindClientWithMaxProject() {
        Map<String, Long> findClientWithMaxProjects = findsClientWithMaxProjects();
        for (Map.Entry<String, Long> project : findClientWithMaxProjects.entrySet()) {
            System.out.println("name: " + project.getKey());
            System.out.println("projectCount: " + project.getValue());
        }
        System.out.println("---------------------|");
    }

    public void printsFindAnWorkersWithTheMaximumSalary() {
        Map<String, BigDecimal> printMaxSalaryWorker = findsAnWorkersWithTheMaximumSalary();
        for (Map.Entry<String, BigDecimal> listSalary : printMaxSalaryWorker.entrySet()) {
            System.out.println("name: " + listSalary.getKey());
            System.out.println("salary: " + listSalary.getValue());
        }
        System.out.println("---------------------|");
    }

    public void printsFindTheYoungestAndOldestWorkers() {
        List<YoungestOldestWorkers> printYoungestOldestWorkers = findsTheYoungestAndOldestWorkers();
        for (YoungestOldestWorkers listWorkers : printYoungestOldestWorkers) {
            System.out.println("text: " + listWorkers.getText());
            System.out.println("name: " + listWorkers.getName());
            System.out.println("birthday: " + listWorkers.getBirthday());
        }
        System.out.println("---------------------|");
    }

    public void printsFindsPricesForProjects() {
        List<ProjectPrices> printProjectPrices = findsPricesForProjects();
        for (ProjectPrices project : printProjectPrices) {
            System.out.println("workerId: " + project.getWorkerId());
            System.out.println("price: " + project.getPrice());
        }
        System.out.println("---------------------|");
    }

    private List<LongestProject> findingTheLongestProject() {
        List<LongestProject> longestProjects = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSqlFile("sql/find_longest_project.sql"));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LongestProject project = new LongestProject();
                project.setId(resultSet.getLong("id"));
                project.setStartMonth(resultSet.getBigDecimal("start_month"));
                project.setFinishMonth(resultSet.getBigDecimal("finish_month"));
                longestProjects.add(project);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return longestProjects;
    }

    private Map<String, Long> findsClientWithMaxProjects() {
        Map<String, Long> findClientWithMaxProjects = new TreeMap<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSqlFile("sql/find_max_projects_client.sql"));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Long projectCount = resultSet.getLong("project_count");
                findClientWithMaxProjects.put(name, projectCount);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return findClientWithMaxProjects;
    }

    private Map<String, BigDecimal> findsAnWorkersWithTheMaximumSalary() {
        Map<String, BigDecimal> maxSalaryWorker = new TreeMap<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSqlFile("sql/find_max_salary_worker.sql"));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                maxSalaryWorker.put(name, salary);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return maxSalaryWorker;
    }

    private List<YoungestOldestWorkers> findsTheYoungestAndOldestWorkers() {
        List<YoungestOldestWorkers> youngestOldestWorkers = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSqlFile("sql/find_youngest_eldest_workers.sql"));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                YoungestOldestWorkers project = new YoungestOldestWorkers();
                project.setText(resultSet.getString("type"));
                project.setName(resultSet.getString("name"));
                project.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                youngestOldestWorkers.add(project);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return youngestOldestWorkers;
    }

    private List<ProjectPrices> findsPricesForProjects() {
        List<ProjectPrices> projectPrices = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(readSqlFile("sql/print_project_prices.sql"));
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ProjectPrices project = new ProjectPrices();
                project.setWorkerId(resultSet.getLong("worker_id"));
                project.setPrice(resultSet.getBigDecimal("price"));
                projectPrices.add(project);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return projectPrices;
    }

    private String readSqlFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}