package lk.ijse.dep10.editor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lk.ijse.dep10.editor.util.SearchResult;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAndReplaceSceneController {

    @FXML
    private Button btnFind;

    @FXML
    private Button btnReplace;

    @FXML
    private Button btnReplaceAll;

    @FXML
    private CheckBox chkMatchCase;

    @FXML
    private TextField txtFind;

    @FXML
    private TextField txtReplaceWith;
    ArrayList<SearchResult> searchResultList = new ArrayList<>();
    private int pos;
    private TextArea txtEditor;

    @FXML
    public void initData(TextArea txtEditor) {
        this.txtEditor = txtEditor;

    }

    public void initialize() {
        txtFind.textProperty().addListener((observableValue, old, current) -> {
            findResultCount();
        });
        btnReplace.setDisable(true);
    }

    private void findResultCount() {
        pos = -1;
        searchResultList.clear();
        Pattern pattern;
        try {
            if (chkMatchCase.isSelected()){
                pattern=Pattern.compile(txtFind.getText(),Pattern.CASE_INSENSITIVE);
            }else{
                pattern = Pattern.compile(txtFind.getText() );
            }
        } catch (Exception e) {
            return;
        }
        Matcher matcher = pattern.matcher(txtEditor.getText());
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            searchResultList.add(new SearchResult(start, end));
        }

    }

    private void select() {
        if (searchResultList.isEmpty()) return;
        SearchResult searchResult = searchResultList.get(pos);
        txtEditor.selectRange(searchResult.getStart(), searchResult.getEnd());
    }

    @FXML
    void btnFindOnAction(ActionEvent event) {
        pos++;
        if (pos == searchResultList.size()) {
            pos = -1;
            return;
        }
        select();
        if(txtReplaceWith.getText().isBlank()){
            btnReplace.setDisable(true);
        }else{
            btnReplace.setDisable(false);
        }


    }
    @FXML
    void btnReplaceAllOnAction(ActionEvent event) {
        txtEditor.textProperty().unbind();
        txtEditor.setText( txtEditor.getText().replaceAll(txtFind.getText(), txtReplaceWith.getText()));
    }

    @FXML
    void btnReplaceOnAction(ActionEvent event) {
        txtEditor.textProperty().unbind();
        SearchResult searchResult = searchResultList.get(pos);
        txtEditor.replaceText(searchResult.getStart(), searchResult.getEnd(),txtReplaceWith.getText());
        findResultCount();
        btnReplace.setDisable(true);
    }

}
