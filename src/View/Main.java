package View;
import Model.MyModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.File;
import java.util.Optional;



public class Main extends Application {

    public static Stage mainStage;
    public static FXMLLoader mainLoader;
    public static MyViewController mainController;
    private static File path1 = new File("resources/music/entry.mp3");
    private static Media media1 = new Media(path1.toURI().toString());
    public static MediaPlayer entryMusic = new MediaPlayer(media1);



    @Override
    public void start(Stage primaryStage) throws Exception{
        //--------------
        primaryStage.setTitle("Maze Game");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("MyView.fxml"));
        primaryStage.getIcons().add(new Image("./resources/images/icon.jpg"));
        Scene scene = new Scene(fxmlLoader.load());
        mainLoader = fxmlLoader;
        mainStage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.show();
        entryMusic.play();
    }


    private void SetStageCloseEvent(Stage primaryStage, MyModel myModel) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    // Close program
                    myModel.stopServers();
                    primaryStage.close();
                    Platform.exit();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
