package lk.ijse.dep10.editor.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
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
    private File openFile;
    private File saveFile;
    private File previousSavedFile;
    private boolean isNewFile;
    @FXML
    void initialize() {
        isSaved = false;
        isNewFile=true;
        openFile=null;
        saveFile=null;

    }

    @FXML
    void mnCloseOnAction(ActionEvent event) {
        if (!isSaved) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to close document ?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                Platform.exit();
                return;
            }
            return;

        }
        Platform.exit();

    }

    @FXML
    void mnNewOnAction(ActionEvent event) {
        isNewFile=true;
        openFile=null;
        saveFile=null;

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
        openFile = fileChooser.showOpenDialog(txtEditor.getScene().getWindow());
        if (openFile == null) return;
        FileInputStream fis = new FileInputStream(openFile);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        txtEditor.setHtmlText(new String(bytes));

        Stage stage= (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(openFile.getName());
    }

    @FXML
    void mnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void mnSaveOnAction(ActionEvent event) throws IOException {

        if (isNewFile){
            FileChooser fileChooser=new FileChooser();
            saveFile = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (saveFile ==null) return;
            FileOutputStream fos=new FileOutputStream(saveFile);
            String text=txtEditor.getHtmlText();
            fos.write(text.getBytes());
            fos.close();


        }else  {
            if (saveFile==null) saveFile=openFile;
            FileOutputStream fos = new FileOutputStream(saveFile);
            String text = txtEditor.getHtmlText();
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();

        }
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(saveFile.getName());
        isNewFile=false;
        previousSavedFile=saveFile;


    }

    public void rootOnDragOver(DragEvent dragEvent) {
        dragEvent.acceptTransferModes(TransferMode.ANY);
    }

    public void rootOnDragDropped(DragEvent dragEvent) throws IOException {
        File droppedFile = dragEvent.getDragboard().getFiles().get(0);
        FileInputStream fis = new FileInputStream(droppedFile);
        byte[] bytes = fis.readAllBytes();
        fis.close();
        txtEditor.setHtmlText(new String(bytes));
    }

    public void mnAboutOnAction(ActionEvent event) {
    }

    public void txtEditorOnKeyPressed(KeyEvent keyEvent) {
        isSaved = false;
        Stage stage = (Stage) txtEditor.getScene().getWindow();
        if (openFile!= null){
            stage.setTitle("*"+openFile.getName());
        }else if (saveFile !=null){
            stage.setTitle("*"+saveFile.getName());
        }else {
            stage.setTitle("*Untitled Document");
        }

    }

    public void mnSaveAsOnAction(ActionEvent event) throws IOException {
        isSaved = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a text file");
        saveFile = fileChooser.showSaveDialog(txtEditor.getScene().getWindow());

        if (saveFile == null){
            saveFile=previousSavedFile;
            return;
        }
        FileOutputStream fos = new FileOutputStream(saveFile);
        String text = txtEditor.getHtmlText();
        byte[] bytes = text.getBytes();
        fos.write(bytes);
        fos.close();

        Stage stage = (Stage) txtEditor.getScene().getWindow();
        stage.setTitle(saveFile.getName());


    }

    public void txtEditorOnMouseClicked(MouseEvent mouseEvent) {
    }
}
