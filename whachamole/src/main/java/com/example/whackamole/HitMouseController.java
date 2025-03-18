package com.example.whackamole;

import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

//Interface controller for the game of Whack-a-mole
public class HitMouseController implements Initializable {

    @FXML
    private Label labelTime; // Timing label
    @FXML
    private Button btnStart; // Start button
    @FXML
    private Label labelCount; // counting label
    @FXML
    private GridPane gpGrass; // A grid pane of grass

    private Button[][] btnArray = new Button[4][5]; //the hole button array

    // get a Hole button
    private Button getHoleView() {
        Button btn = new Button(); // create a button
        btn.setPadding(new Insets(0, 0, 0, 0)); // Set the white space to 0 around the button
        btn.setGraphic(new ImageView(imageHole)); // Set the image to hole.png for the button
        return btn;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization after the UI is opened
        // initialize each hole button and set the click event for each hole button
        for (int i = 0; i < btnArray.length; i++) {
            for (int j = 0; j < btnArray[i].length; j++) {
                btnArray[i][j] = getHoleView(); // get a Hole button
                Button view = btnArray[i][j];
                gpGrass.add(view, j, i + 1); // add the hole button to the grass grid
                int x = i, y = j;
                // Sets the action event for the hole button.
                // Clicking a hole in the ground means swinging the hammer to whack a mouse
                // default kick the hole
                view.setOnAction(e -> doAction(x, y, TYPE_HOLE_HIT));
            }
        }
        labelTime.setFont(Font.font("KaiTi", 25));
        btnStart.setFont(Font.font("KaiTi", 25));
        labelCount.setFont(Font.font("KaiTi", 25));
        btnStart.setOnAction(e -> { // event handler after clicking the start button
            isRunning = !isRunning;
            if (isRunning) { // if the game state is running
                btnStart.setText("Stop");
                hitCount = 0; // clear hitCount
                timeCount = 0; // clear timeCount
                beginTime = new Date().getTime(); // get the beginning time
                new MouseThread(0).start(); // start the first mouse thread
                new MouseThread(timeUnit * 1).start(); // start the second mouse thread
                new MouseThread(timeUnit * 2).start(); // start the third mouse thread
            } else { // game over
                btnStart.setText("Start");
            }
        });
    }

    private boolean isRunning = false; // the game state is running or not
    private long beginTime; // the beginning time
    private int timeCount = 0;
    private int hitCount = 0;
    private int timeUnit = 1000; // 1,000 milliseconds = 1 second
    // Define a countdown array corresponding to the hole button array.
    // size:4*5
    private int[][] timeArray = {{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}};

    // define the mouse thread
    private class MouseThread extends Thread {

        private int mDelay; // Delay interval

        public MouseThread(int delay) {
            mDelay = delay;
        }

        public void run() {
            try {
                sleep(mDelay); // different mouse has different delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isRunning) { // the game state is running
                int i = 0, j = 0;
                while (true) {
                    // Randomly generate the position where the mouse appears
                    i = new Random().nextInt(btnArray.length);
                    j = new Random().nextInt(btnArray[0].length);
                    if (timeArray[i][j] == 0) {
                        //do some action when the mouse go out the hole
                        doAction(i, j, TYPE_MOUSE);
                        break;
                    }
                }
                long nowTime = new Date().getTime();
                timeCount = (int) ((nowTime - beginTime) / 1000);
                try {
                    sleep((timeUnit - 100) * 3); // the time of the mouse stay out the hole
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // do some actions when the hole changes its state
    private synchronized void doAction(int i, int j, int type) {
        timeArray[i][j] = 3; // The mouse will stay out the hole for 3 seconds
        Button btn = btnArray[i][j];
        if (type == TYPE_HOLE_HIT) {
            showView(btn, imageHoleHit); // Show image of hit hole
            timeSchedule(i, j); // The hole timer began to count down
        } else if (type == TYPE_MOUSE) {
            showView(btn, imageMouse); // Show mouse image
            timeSchedule(i, j); // The hole timer began to count down
            btn.setOnAction(e -> { // Register the click event for the hole button
                doAction(i, j, TYPE_MOUSE_HIT); // Once the mouse in the hole is hit, do the TYPE_MOUSE_HIT action
                hitCount++; // Update hitCount
            });
        } else if (type == TYPE_MOUSE_HIT) {
            showView(btn, imageMouseHit);  // Show image of hit mouse
            btn.setOnAction(null); // Unregister the click event for the hole button
        }
    }

    // Countdown time
    private void timeSchedule(int i, int j) {
        Button btn = btnArray[i][j];
        Timer timer = new Timer();
        timer.schedule(new TimerTask() { // The timer is scheduled once per second
            public void run() {
                timeArray[i][j]--;
                if (timeArray[i][j] <= 0) { // time out
                    showView(btn, imageHole); // show empty hole
                    btn.setOnAction(e -> { // Registers the click event for the hole
                        doAction(i, j, TYPE_HOLE_HIT);
                    });
                    timer.cancel(); // Cancel the timer
                }
            }
        }, 0, timeUnit);
    }

    // show the image of the hole
    private void showView(Button btn, Image image) {
        // define a JavaFX Task
        // The call method of a task cannot manipulate the interface;
        // the succeeded method does
        Task task = new Task<Void>() {

            // The thread inside the call method is not the main thread
            // and cannot manipulate the interface
            protected Void call() throws Exception {
                return null;
            }

            // The thread inside the succeeded method is the main thread
            //can manipulate the interface
            protected void succeeded() {
                super.succeeded();
                btn.setGraphic(new ImageView(image)); // Set the button image as the input image
                labelCount.setText(String.format("Hit %d mice", hitCount));
                labelTime.setText(String.format("%02d:%02d", timeCount / 60, timeCount % 60));
            }
        };
        task.run(); // start the JavaFX task
    }

    private final static int TYPE_HOLE = 1; // hole
    private final static int TYPE_MOUSE = 2; // mouse
    private final static int TYPE_MOUSE_HIT = 3; // hit the mouse
    private final static int TYPE_HOLE_HIT = 4; // hit the hole
    private static Image imageHole; // hole image
    private static Image imageMouse; // mouse image
    private static Image imageMouseHit; // image of hit mouse
    private static Image imageHoleHit; // image of hit hole

    static {
        imageHole = new Image(HitMouseController.class.getResourceAsStream("hole.png"));
        imageMouse = new Image(HitMouseController.class.getResourceAsStream("mouse.png"));
        imageMouseHit = new Image(HitMouseController.class.getResourceAsStream("mouse_hit.png"));
        imageHoleHit = new Image(HitMouseController.class.getResourceAsStream("hole_hit.png"));
    }

}
