package lk.ijse.dep10.editor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lk.ijse.dep10.editor.util.SearchResult;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindSceneController {

    public TextField txtFind;
    @FXML
    private Button btnDown;

    @FXML
    private Button btnUp;
    ArrayList <SearchResult> searchResultList=new ArrayList<>();
    private int pos=-1;
    private TextArea txtEditor;
    @FXML
    public void initData(TextArea txtEditor){
        this.txtEditor=txtEditor;

    }
    public void initialize(){
        txtFind.textProperty().addListener((observableValue, old, current) ->{
            findResultCount();
        } );
    }

    private void findResultCount() {
        pos=-1;
        searchResultList.clear();
//        txtEditor.clear();
        Pattern pattern;
        try {
            pattern = Pattern.compile(txtFind.getText());
        } catch (Exception e) {
            return;
        }
        Matcher matcher = pattern.matcher(txtEditor.getText());
        while(matcher.find()){
            int start = matcher.start();
            int end = matcher.end();
            searchResultList.add(new SearchResult(start,end));
        }
    }
    private void select(){
        if (searchResultList.isEmpty()) return;
        SearchResult searchResult = searchResultList.get(pos);
        txtEditor.selectRange(searchResult.getStart(), searchResult.getEnd());
    }

    @FXML
    void btnDownOnAction(ActionEvent event) {
        pos++;
        if (pos== searchResultList.size()){
            pos=-1;
            return;
        }
        select();
    }

    @FXML
    void btnUpOnAction(ActionEvent event) {
        pos--;
        if (pos< 0){
            pos=searchResultList.size();
            return;
        }
        select();
    }

}
