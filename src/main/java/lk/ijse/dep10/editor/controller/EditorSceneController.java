package lk.ijse.dep10.editor.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.Optional;

public class EditorSceneController {

    public AnchorPane root;
    public MenuItem mnAbout;
    public MenuItem mnSaveAs;
    public MenuItem mnFind;
    public MenuItem mnFindAndReplace;
    @FXML
    private MenuItem mnClose;

    @FXML
    private MenuItem mnNew;

    @FXML
    private MenuItem mnOpen;

    @FXML
    private MenuItem mnPrint;

    @FXML
    private MenuItem mnSave;

    @FXML
    public TextArea txtEditor;
    private boolean isSaved;
    private boolean isNewFile;
    private File file;
    private File previousSavedFile;
    private Stage stage;
    private Stage stageFind;
    private Stage stageFindAndReplace;
    @FXML
    void initialize() {
        isSaved = false;
        file = null;
        isNewFile = true;
        Platform.runLater(() -> {
            stage = (Stage) txtEditor.getScene().getWindow();
            windowCloseRequest();
        });

    }

    public void windowCloseRequest() {

        stage.setOnCloseRequest(windowEvent -> {
            showAlertMessage(stage, windowEvent);
        });
    }

    @FXML
    void mnCloseOnAction(ActionEvent event) {

        showAlertMessage(stage, event);

    }

    public void showAlertMessage(Stage stage, Event event) {
        if (!isSaved) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to close document ?",
                    ButtonType.YES, ButtonType.CANCEL);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                stage.close();
            }
            event.consume();
        } else {
            stage.close();
        }
    }


    @FXML
    void mnNewOnAction(ActionEvent event) {
        file = null;
        isNewFile = true;
        if (!isSaved) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to open a new document ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                txtEditor.setText("");
                return;
            }
            return;
        }
        txtEditor.setText("");
    }

    @FXML
    void mnOpenOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a text file");
        file = fileChooser.showOpenDialog(stage);
        if (file == null) return;
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] bytes = bis.readAllBytes();
        txtEditor.setText(new String(bytes));
        bis.close();
        stage.setTitle(file.getName());
    }


    @FXML
    void mnSaveOnAction(ActionEvent event) throws IOException {
        if (isNewFile) {
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (file == null) return;
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos=new BufferedOutputStream(fos);
            String text = txtEditor.getText();
            bos.write(text.getBytes());
            bos.close();
            isNewFile = false;
        } else {
            FileOutputStream fos = new FileOutputStream(file);
            String text = txtEditor.getText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();

        }
        stage.setTitle(file.getName());
        previousSavedFile = file;


    }

    public void mnAboutOnAction(ActionEvent event) throws IOException {
        URL file = this.getClass().getResource("/view/HelperScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(file);
        AnchorPane root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("About");
        stage.show();
    }

    public void txtEditorOnKeyPressed(KeyEvent keyEvent) {


        isSaved = false;
        if (file != null) {
            stage.setTitle("*" + file.getName());
        } else {
            stage.setTitle("*Untitled Document");
        }

    }

    public void mnSaveAsOnAction(ActionEvent event) throws IOException {
        isSaved = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a text file");
        file = fileChooser.showSaveDialog(stage);

        if (file == null) {
            file = previousSavedFile;
            return;
        }
        FileOutputStream fos = new FileOutputStream(file);
        String text = txtEditor.getText();
        byte[] bytes = text.getBytes();
        fos.write(bytes);
        fos.close();

        stage.setTitle(file.getName());
    }


    public void txtEditorOnDragDropped(DragEvent dragEvent) throws IOException {
        File droppedFile = dragEvent.getDragboard().getFiles().get(0);
        FileInputStream fis = new FileInputStream(droppedFile);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        txtEditor.setText(new String(bytes));
    }

    public void txtEditorOnDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.ANY);
    }

    public void rootOnKeyPressed(KeyEvent keyEvent) {
        boolean ctrl = false;
        boolean s = false;
        boolean o = false;
        boolean n = false;
        boolean shift = false;
        boolean p = false;
        boolean w = false;
        if (keyEvent.getCode() == (KeyCode.CONTROL)) {
            ctrl = true;
        }
        if (keyEvent.getCode() == (KeyCode.S)) {
            s = true;
        }
        if (keyEvent.getCode() == (KeyCode.O)) {
            o = true;
        }
        if (keyEvent.getCode() == (KeyCode.N)) {
            n = true;
        }
        if (keyEvent.getCode() == (KeyCode.SHIFT)) {
            shift = true;
        }
        if (keyEvent.getCode() == (KeyCode.P)) {
            p = true;
        }
        if (keyEvent.getCode() == (KeyCode.W)) {
            w = true;
        }
        if (ctrl && n) mnNew.fire();
        if (ctrl && s) mnSave.fire();
        if (ctrl && shift && s) mnSaveAs.fire();
        if (ctrl && o) mnOpen.fire();
        if (ctrl && p) mnPrint.fire();
        if (ctrl && w) mnClose.fire();
    }

    public void mnFindOnAction(ActionEvent actionEvent) throws IOException {
        if (stageFind !=null) return;
        stageFind=new Stage();
        FXMLLoader fxmlLoader=new FXMLLoader(this.getClass().getResource("/view/FindScene.fxml"));
        AnchorPane root=fxmlLoader.load();
        stageFind.setScene(new Scene(root));
        stageFind.setOnCloseRequest(windowEvent -> {stageFind=null;});
        stageFind.initModality(Modality.WINDOW_MODAL);
        stageFind.initStyle(StageStyle.UNDECORATED);

        stageFind.setX(stage.getX()+stage.getWidth()-400.0);

        stageFind.setY(stage.getY()+68.0);
        stageFind.show();

        FindSceneController ctrl = fxmlLoader.getController();
        SimpleStringProperty observable=new SimpleStringProperty(txtEditor.getText());
        txtEditor.textProperty().bind(observable);
        ctrl.initData(txtEditor);

    }

    public void mnFindAndReplaceOnAction(ActionEvent actionEvent) throws IOException {
        if (stageFindAndReplace !=null) return;
        stageFindAndReplace=new Stage();
        FXMLLoader fxmlLoader=new FXMLLoader(this.getClass().getResource("/view/FindAndReplaceScene.fxml"));
        AnchorPane root=fxmlLoader.load();
        stageFindAndReplace.setScene(new Scene(root));
        stageFindAndReplace.setOnCloseRequest(windowEvent -> {
            stageFindAndReplace=null;
            txtEditor.textProperty().unbind();
        });
        stageFindAndReplace.setTitle("Find and Replace");
        stageFindAndReplace.initModality(Modality.WINDOW_MODAL);
        stageFindAndReplace.centerOnScreen();
        stageFindAndReplace.show();

        FindAndReplaceSceneController ctrl = fxmlLoader.getController();
        SimpleStringProperty observable=new SimpleStringProperty(txtEditor.getText());
        txtEditor.textProperty().bind(observable);
        ctrl.initData(txtEditor);
    }

    public void rootOnMouseClicked(MouseEvent mouseEvent) {

    }

    public void txtEditorOnMouseClicked(MouseEvent mouseEvent) {
        if (!(stageFind==null)){
            stageFind.close();
            stageFind=null;
        }
    }
}
