package swapstones.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import gameresult.OnePlayerGameResult;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import util.DurationUtil;

public class TableViewController {

    private static final int NUMBER_OF_ROWS_TO_SHOW = 15;

    @FXML
    private TableView<OnePlayerGameResult> tableView;

    @FXML
    private TableColumn<OnePlayerGameResult, String> playerName;

    @FXML
    private TableColumn<OnePlayerGameResult, Integer> numberOfMoves;

    @FXML
    private TableColumn<OnePlayerGameResult, String> duration;

    @FXML
    private TableColumn<OnePlayerGameResult, String> created;

    @FXML
    private void initialize() throws IOException {
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        numberOfMoves.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));
        duration.setCellValueFactory(
                cellData -> {
                    var duration = cellData.getValue().getDuration();
                    return new ReadOnlyStringWrapper(DurationUtil.formatDuration(duration));
                });
        created.setCellValueFactory(
                cellData -> {
                    var dateTime = cellData.getValue().getCreated();
                    var formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
                    return new ReadOnlyStringWrapper(formatter.format(dateTime));
                }
        );
        ObservableList<OnePlayerGameResult> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonOnePlayerGameResultManager(Path.of("one-player-results.json")).getBestByNumberOfMoves(NUMBER_OF_ROWS_TO_SHOW));
        tableView.setItems(observableList);
    }

    public void handlePlayAgain(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/userview.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
