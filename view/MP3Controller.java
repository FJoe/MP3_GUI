package view;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import application.AVLNode;
import application.AVLTree;

public class MP3Controller 
{
	@FXML TextField songInput;
	@FXML TextField  artistInput;
	@FXML TextField albumInput;
	@FXML TextField yearInput;
	@FXML Button addSongButton;
	@FXML ListView<AVLNode> listView;
		
//	private AVLTree list;
	private ObservableList<AVLNode> obsList;
	
	public void start()
	{
		obsList = FXCollections.observableArrayList();

	}
	
	public void addSong(ActionEvent e)
	{
		addSongButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) 
			{
				obsList.add(new AVLNode(songInput.getText(), artistInput.getText(), albumInput.getText(), yearInput.getText()));
				listView.setItems(obsList);
			}
		});
	}
}
