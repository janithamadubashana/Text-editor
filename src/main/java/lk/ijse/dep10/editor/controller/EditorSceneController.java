package lk.ijse.dep10.editor.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;

public class EditorSceneController {

    public AnchorPane root;
    public MenuItem mnAbout;
    public MenuItem mnSaveAs;
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
    private HTMLEditor txtEditor;
    private boolean isSaved ;
    private boolean isNewFile ;
    private File file;
    private File previousSavedFile;

    @FXML
    void initialize() {
        isSaved = false;
        file =null;
        isNewFile=true;
    }

    public void windowCloseRequest(Stage stage){
        stage.setOnCloseRequest(windowEvent -> {
            showAlertMessage(stage,windowEvent);
        });
    }
    @FXML
    void mnCloseOnAction(ActionEvent event) {
        Stage stage= (Stage) txtEditor.getScene().getWindow();
        showAlertMessage(stage,event);

    }
    public  void showAlertMessage(Stage stage, Event event){
        if (!isSaved) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to close document ?",
                    ButtonType.YES, ButtonType.CANCEL);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                stage.close();
            }
            event.consume();
        }else {
            stage.close();
        }
    }


    @FXML
    void mnNewOnAction(ActionEvent event) {
        file =null;
        isNewFile=true;
        if (!isSaved) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to open a new document ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                txtEditor.setHtmlText("");
                return;
            }
            return;
        }
        txtEditor.setHtmlText("");
    }

    @FXML
    void mnOpenOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a text file");
        file = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());
        if (file == null) return;
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        txtEditor.setHtmlText(new String(bytes));

        Stage stage= (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(file.getName());
    }

    @FXML
    void mnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void mnSaveOnAction(ActionEvent event) throws IOException {
        if (isNewFile){
            FileChooser fileChooser=new FileChooser();
            file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (file ==null) return;
            FileOutputStream fos=new FileOutputStream(file);
            String text=txtEditor.getHtmlText();
            fos.write(text.getBytes());
            fos.close();
            isNewFile=false;
        }else  {
            FileOutputStream fos = new FileOutputStream(file);
            String text = txtEditor.getHtmlText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();

        }
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(file.getName());
        previousSavedFile=file;


    }
    public void mnAboutOnAction(ActionEvent event) throws IOException {
        URL file=this.getClass().getResource("/view/HelperScene.fxml");
        FXMLLoader fxmlLoader=new FXMLLoader(file);
        AnchorPane root=fxmlLoader.load();

        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("About");
        stage.show();
    }

    public void txtEditorOnKeyPressed(KeyEvent keyEvent) {
        isSaved = false;
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        if (file != null){
            stage.setTitle("*"+ file.getName());
        }else {
            stage.setTitle("*Untitled Document");
        }

    }

    public void mnSaveAsOnAction(ActionEvent event) throws IOException {
        isSaved = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a text file");
        file = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());

        if (file == null){
            file=previousSavedFile;
            return;
        }
        FileOutputStream fos = new FileOutputStream(file);
        String text = txtEditor.getHtmlText();
        byte[] bytes = text.getBytes();
        fos.write(bytes);
        fos.close();

        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(file.getName());
    }


    public void txtEditorOnDragDropped(DragEvent dragEvent) throws IOException {
        File droppedFile = dragEvent.getDragboard().getFiles().get(0);
        FileInputStream fis = new FileInputStream(droppedFile);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        txtEditor.setHtmlText(new String(bytes));
    }

    public void txtEditorOnDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.ANY);
    }

    public void rootOnKeyPressed(KeyEvent keyEvent) {
        boolean ctrl=false;
        boolean s=false;
        boolean o=false;
        boolean n=false;
        boolean shift=false;
        boolean p=false;
        boolean w=false;
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
}
