package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;

public class Main extends Application {

    private boolean isServer = false;
    private BizingoSocket connection = isServer ? createServer() : createClient();

    Controller cont;
    private Controller getController(Controller controller){
        return this.cont = controller;
    }
    /*private TextArea messages = new TextArea();

    private Parent createContent() {
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");
            try {
                connection.send(message);
            } catch (Exception e) {
                messages.appendText("Failed to send \n");
            }
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600,600);
        return root;
    }*/


    @Override
    public void init() throws Exception{
        connection.startConnection();
    }


    /*
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Bizingo");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();

    }*/

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Bizingo");
        primaryStage.setScene(new Scene(root, 1008, 602));


        Controller ctrl = (Controller)(loader.getController());
        ctrl.getConnection(connection);
        getController(ctrl);
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception{
        connection.closeConnection();
    }



    private Server createServer(){
        return new Server(1234, data -> {
                Platform.runLater(() ->{
                    cont.bizingoTextArea.setText(data.toString());
                    //messages.appendText(data.toString() + "\n");
                });
        });
    }

    private Client createClient(){
        return new Client("127.0.0.1",1234, data -> {
            Platform.runLater(() ->{
                cont.bizingoTextArea.setText(data.toString());
                //messages.appendText(data.toString() + "\n");
            });
        });
    }


    public static void main(String[] args) { launch(args); }
}
