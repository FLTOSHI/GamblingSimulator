package edu.fltoshi.gamblingsimulator.controller;

import edu.fltoshi.gamblingsimulator.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class MainController {

    // Переменные для Рандома
    Integer firstNumber = 0;
    Integer secondNumber = 0;
    Integer thirdNumber = 0;

    // Переменные - счётчики
    double winCount = 0;
    double rollCount = 0;
    double winToRoll = 0;

    // Элементы FXML
    @FXML
    private Text GamblingNumberOne;

    @FXML
    private Text GamblingNumberThree;

    @FXML
    private Text GamblingNumberTwo;

    @FXML
    private Text rollCountText;

    @FXML
    private Text winCountText;

    @FXML
    private Text winToRollText;

    @FXML
    private Button exitButton;

    @FXML
    private Button rollButton;

    // Чтение данных из текстовых файлах при запуске и впиндюливание значений в них их заместо нулей
    @FXML
    protected void initialize() {
        try {
            String rolls = Files.readString(Paths.get("rolls.txt"));

            rollCount = Double.parseDouble(rolls);
            rollCountText.setText(String.valueOf(rollCount));

            String wins = Files.readString(Paths.get("wins.txt"));

            winCount = Double.parseDouble(wins);
            winCountText.setText(String.valueOf(winCount));

            String winsToRolls = Files.readString(Paths.get("wintoroll.txt"));

            winToRoll = Double.parseDouble(winsToRolls);
            winToRollText.setText(String.valueOf(winToRoll));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Actions
    @FXML
    protected void onRollButtonClick() {
        gamblingAction();
    }

    @FXML
    protected void setExitButtonClick() {
        markToTXT();

        Stage mainWindow = (Stage) exitButton.getScene().getWindow();
        mainWindow.close();
    }


    // Сама игра
    @FXML
    void gamblingAction() {
        Random numberPicker = new Random();
        firstNumber = numberPicker.nextInt(9);
        secondNumber = numberPicker.nextInt(9);
        thirdNumber = numberPicker.nextInt(9);

        GamblingNumberOne.setText(String.valueOf(firstNumber));
        GamblingNumberTwo.setText(String.valueOf(secondNumber));
        GamblingNumberThree.setText(String.valueOf(thirdNumber));

        oneSeven();
        threeSeven();

        rollCounter();
        winCounter();
        winToRollCounter();
        markToTXT();
    }

    // Различные исходы
    // Исход "одна семёрка"
    @FXML
    void oneSeven (){
        if (firstNumber == 7 || secondNumber == 7 || thirdNumber == 7) {
            Application.showWinDialog("win-view.fxml", "Счастливая семёрка");
            winCount++;
        }
    }

    // Исход "три семёрки"
    @FXML
    void threeSeven (){
        if (firstNumber == 7 && secondNumber == 7 && thirdNumber == 7) {
            Application.showWinDialog("win-view.fxml", "ДЖЕКПОТ!");
            winCount++;
        }
    }

    // Счётчик попыток
    @FXML
    void rollCounter() {
        rollCount++;
        rollCountText.setText(String.valueOf(rollCount));
    }

    // Счётчик побед
    @FXML
    void winCounter() {
        winCountText.setText(String.valueOf(winCount));
    }

    // Соотношение побед к попыткам
    @FXML
    protected void winToRollCounter() {
        winToRoll = Math.round(winCount / rollCount * 100);
        winToRollText.setText(String.valueOf(winToRoll));
    }

    // Сохранение данных в текстовый файл
    @FXML
    protected void markToTXT(){
            PrintWriter fw = null;
            PrintWriter fw2 = null;
            PrintWriter fw3 = null;

            try {
                fw = new PrintWriter("rolls.txt");
                fw2 = new PrintWriter("wins.txt");
                fw3 = new PrintWriter("wintoroll.txt");

                BufferedWriter bw = new BufferedWriter(fw);
                BufferedWriter bw2 = new BufferedWriter(fw2);
                BufferedWriter bw3 = new BufferedWriter(fw3);

                bw.write(rollCountText.getText());
                bw2.write(winCountText.getText());
                bw3.write(winToRollText.getText());

                bw.close();
                bw2.close();
                bw3.close();

            } catch (IOException e) {
                e.printStackTrace();
                fw.close();
                fw2.close();
                fw3.close();
            }
    }
}