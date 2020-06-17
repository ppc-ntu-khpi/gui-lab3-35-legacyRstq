  
package com.mybank.gui;

import com.mybank.data.DataSource;
import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class FXDemo extends Application {

    private Text title;
    private Text details;
    private ComboBox clients;

    @Override
    public void start(Stage primaryStage) {

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(addVBox());
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        border.setLeft(scrollPane);
        addStackPane(hbox);

        Scene scene = new Scene(border, 310, 500);

        primaryStage.setTitle("MyBank Clients");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:images/icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        
        title = new Text("Client Name");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        vbox.getChildren().add(title);

        Line separator = new Line(10, 10, 280, 10);
        vbox.getChildren().add(separator);

        details = new Text("Account:");
        details.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        vbox.getChildren().add(details);

        return vbox;
    }

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            items.add(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }

        clients = new ComboBox(items);
        clients.setPrefSize(150, 20);
        clients.setPromptText("Click to choose...");

        Button buttonShow = new Button("Show");
        buttonShow.setPrefSize(100, 20);

        buttonShow.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    int custNo = clients.getItems().indexOf(clients.getValue());
                    title.setText(clients.getValue().toString());
                    Customer customer = Bank.getCustomer(custNo);
                    details.setText(getCustomerDetails(customer));
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error getting client...");
                    // Header Text: null
                    alert.setHeaderText(null);
                    String details = e.getMessage() != null ? e.getMessage() : "You need to choose a client first!";
                    alert.setContentText("Error details: " + details);
                    alert.showAndWait();
                }
            }
        });
        
        Button buttonReport = new Button("Report");
        buttonReport.setPrefSize(100, 20);
        
        buttonReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                StringBuilder customersReport = new StringBuilder();
                title.setText("Report:");
                details.setText(getReport());
            }
        });

        hbox.getChildren().addAll(clients, buttonShow, buttonReport);

        return hbox;
    }

    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                    new Stop(0, Color.web("#4977A3")),
                    new Stop(0.5, Color.web("#B0C6DA")),
                    new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        helpIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ShowAboutInfo();
            }
        });

        helpText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ShowAboutInfo();
            }
        });
        
        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }
    private void ShowAboutInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Just a simple JavaFX demo.\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich");
        alert.showAndWait();
    }
    
    private String getCustomerDetails(Customer customer){
        StringBuilder details = new StringBuilder();
        for(int i = 0; i < customer.getNumberOfAccounts(); i++){
            Account account = customer.getAccount(i);
            if(account != null){
                if (account instanceof SavingsAccount) {
                    details.append("Account Type: 'Savings'")
                    .append("\nBalance: ").append(account.getBalance()).append("\n\n");
                } 
                else if (account instanceof CheckingAccount) {
                    details.append("Account Type: 'Checking' ")
                    .append("\nBalance: ").append(account.getBalance()).append("\n");
                }
            }
        }
        return details.toString();
    }
    
    private String getReport(){
        StringBuilder report = new StringBuilder();
        for(int i = 0; i < Bank.getNumberOfCustomers(); i++){
            Customer customer = Bank.getCustomer(i);
            report.append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n\n");
            report.append(getCustomerDetails(customer)).append("-------------------------\n");
        }
        
        return report.toString();
    }
    
    public static void main(String[] args) throws IOException {
        DataSource dataSource = new DataSource("data/test.dat");
        dataSource.loadData();

        launch(args);
    }

}