import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Random;

/**@author Srijan Ponnala
 * @version 4/11/22
 */
public class Jordle extends Application {
    private String current;
    private int count = 0;
    private String magicWord = Words.list.get(new Random().nextInt(Words.list.size()));
    private Text centerText = null;
    private Text status = null;
    private boolean dark = false;

    /**
     * @param args a String[] that contains the list of arguments needed to run the program
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Button instrButton = new Button("Instructions");
        Button restartButton = new Button("Restart");
        Button submit = new Button("Submit");
        Button darks = new Button("Dark Mode");
        Button lights = new Button("Light Mode");

        instrButton.setFocusTraversable(false);
        restartButton.setFocusTraversable(false);
        submit.setFocusTraversable(false);
        darks.setFocusTraversable(false);
        lights.setFocusTraversable(false);

        centerText = new Text("Jordle");
        status = new Text("Try guessing a word!");
        TextField inp = new TextField();
        status.setStyle("-fx-font: 14 arial;");

        instrButton.setOnAction(e -> Window.display("Instructions",
                "Welcome to Jordle! Try guessing some 5 letter words!"
                + " After each entry, the program will give you feedback! "
                + "There is also a dark mode, try it out!"));

        HBox h = new HBox(darks);
        h.setAlignment(Pos.BASELINE_RIGHT);
        HBox hb1 = new HBox(inp, submit);
        hb1.setSpacing(20);
        hb1.setAlignment(Pos.BASELINE_CENTER);
        HBox hBox = new HBox(status, restartButton, instrButton);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        VBox vBox = new VBox(hb1, hBox);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
        Scene scene = new Scene(root);

        GridPane pane = new GridPane();

        double s = 60;
        pane.setHgap(10);
        pane.setVgap(10);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle r = new Rectangle(s, s, s, s);
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(1.5);
                StackPane stack = new StackPane();
                stack.getChildren().addAll(r);
                pane.add(stack, j, i);
            }
        }
        pane.setAlignment(Pos.CENTER);
        root.setBottom(vBox);
        root.setCenter(pane);
        GridPane g1 = new GridPane();
        g1.setHgap(250);
        centerText.setStyle("-fx-font: 30 arial;");
        centerText.setTextAlignment(TextAlignment.RIGHT);
        darks.setAlignment(Pos.BASELINE_RIGHT);
        lights.setAlignment(Pos.BASELINE_RIGHT);
        g1.add(lights, 0, 0);
        g1.add(centerText, 1, 0);
        g1.add(darks, 2, 0);
        g1.setAlignment(Pos.CENTER_RIGHT);
        root.setTop(g1);
        root.setPrefSize(780, 600);
        root.setStyle("-fx-padding: 20;");

        lights.setOnAction(e -> makeLight(root));
        darks.setOnAction(e -> makeDark(root));
        submit.setOnAction(e -> {
            addInput(stage, root, pane, inp.getText()); inp.clear();
        });
        inp.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                addInput(stage, root, pane, inp.getText());
                inp.clear();
            }
        });
        restartButton.setOnAction(e -> resetBoard(stage));

        scene.setFill(Color.BEIGE);
        stage.setScene(scene);
        stage.setTitle("Jordle");
        stage.show();
    }

    private void addInput(Stage st, BorderPane bp, GridPane gp, String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (s.length() != 5) {
            alert.setContentText("Please enter a 5 letter word");
            alert.getDialogPane();
            alert.show();
        } else {
            if (s.matches("^[a-zA-Z]*$")) {
                current = s.toUpperCase();
                for (int i = 0; i < 5; i++) {
                    Rectangle re = new Rectangle(60, 60, 60, 60);
                    if (magicWord.indexOf(current.toLowerCase().charAt(i)) != -1) {
                        re.setFill(Color.YELLOW);
                        if (magicWord.charAt(i) == current.toLowerCase().charAt(i)) {
                            re.setFill(Color.GREEN);
                        }
                    } else {
                        re.setFill(Color.DARKGRAY);
                    }
                    re.setStroke(Color.BLACK);
                    re.setStrokeWidth(1.5);
                    Text txt = new Text(String.valueOf(current.charAt(i)));
                    txt.setStyle("-fx-font: 18 arial;");
                    StackPane sta = new StackPane();
                    sta.getChildren().addAll(re, txt);
                    gp.add(sta, i, count);
                }
                count++;
                checkWord(st, bp, current);
            } else {
                alert.setContentText("Please do not use special characters or numbers");
                alert.getDialogPane();
                alert.show();
            }
        }
    }

    private void checkWord(Stage sta, BorderPane bor, String str) {
        if (str.toLowerCase().equals(magicWord)) {
            setTxt(sta, bor, new Text("Congratulations! You've guessed the word!"));
        } else if (count > 5) {
            setTxt(sta, bor, new Text("Game over. The word was " + magicWord + "."));
        }
    }

    private void setTxt(Stage stag, BorderPane br, Text t) {
        Button instruButton = new Button("Instructions");
        Button restarButton = new Button("Restart");
        Button submitb = new Button("Submit");
        TextField inp = new TextField();

        instruButton.setFocusTraversable(false);
        restarButton.setFocusTraversable(false);
        submitb.setFocusTraversable(false);

        status = t;
        if (dark) {
            status.setStyle("-fx-font: 14 arial; -fx-fill: white");
        } else {
            status.setStyle("-fx-font: 14 arial; -fx-fill: black");
        }
        instruButton.setOnAction(e -> Window.display("Instructions",
                "Welcome to Jordle! Try guessing some 5 letter words!"
                + " After each entry, the program will give you feedback!" + "There is also a dark mode, try it out!"));
        restarButton.setOnAction(e -> resetBoard(stag));
        HBox hb2 = new HBox(inp, submitb);
        hb2.setSpacing(20);
        hb2.setAlignment(Pos.BASELINE_CENTER);
        HBox hBo = new HBox(status, restarButton, instruButton);
        hBo.setSpacing(20);
        hBo.setAlignment(Pos.BASELINE_CENTER);
        VBox vBo = new VBox(hb2, hBo);
        vBo.setSpacing(20);
        vBo.setAlignment(Pos.CENTER);
        br.setBottom(vBo);
    }

    private void resetBoard(Stage st) {
        count = 0;
        magicWord = Words.list.get(new Random().nextInt(Words.list.size()));
        start(st);
    }

    private void makeDark(BorderPane bp) {
        bp.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(0), Insets.EMPTY)));
        centerText.setStyle("-fx-font: 30 arial; -fx-fill: white");
        status.setStyle("-fx-font: 14 arial; -fx-fill: white");
    }

    private void makeLight(BorderPane bp) {
        bp.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(0), Insets.EMPTY)));
        centerText.setStyle("-fx-font: 30 arial; -fx-fill: black");
        status.setStyle("-fx-font: 14 arial; -fx-fill: black");
    }

    /**
     * a static class window that is used to create a new popup window for the instructions.
     */
    public static class Window {
        /**
         * @param title a String that contains the title of the window
         * @param body a String that contains the body of the message in the window
         */
        public static void display(String title, String body) {
            Stage popUp = new Stage();
            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.setTitle(title);

            Text info = new Text(body);
            info.setStyle("-fx-font: 14 arial;");
            info.setTextAlignment(TextAlignment.JUSTIFY);

            Button button = new Button("Return to game");
            button.setOnAction(e -> popUp.close());

            VBox layout = new VBox(25);
            layout.setPadding(new Insets(10, 50, 50, 50));
            layout.getChildren().addAll(info, button);
            layout.setAlignment(Pos.TOP_CENTER);

            Scene scene1 = new Scene(layout, 400, 300);

            popUp.setScene(scene1);
            info.wrappingWidthProperty().bind(scene1.widthProperty().subtract(15));

            popUp.showAndWait();
        }
    }
}
