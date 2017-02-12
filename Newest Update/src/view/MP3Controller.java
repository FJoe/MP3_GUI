/*
 * CS 213 Assignment 1
 * Created by Dillon Heyck and Francis Joe
 */

package view;

import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;
import java.io.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.SongElement;

public class MP3Controller 
{
	@FXML TextField songInput;
	@FXML TextField artistInput;
	@FXML TextField albumInput;
	@FXML TextField yearInput;
	
	@FXML TextArea songDisplay;
	@FXML TextArea  artistDisplay;
	@FXML TextArea albumDisplay;
	@FXML TextArea yearDisplay;
	
	@FXML Button addSongButton;
	@FXML Button editSongButton;
	@FXML Button deleteSongButton;
	@FXML ListView<SongElement> listView;
		
	private Stage mainStage;
	private ObservableList<SongElement> obsList;
	
	public void start(Stage primaryStage)
	{
		mainStage = primaryStage;
		obsList = FXCollections.observableArrayList();
		Scanner input;
		
		File songList = new File("songList.txt");

		try
		{
			input = new Scanner(songList);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("ERROR FINDING songList.txt");
			//TRY TO OPEN NEW songList.txt here
			return;
		}
		
		while(input.hasNextLine())
		{
			System.out.println("In while loop");
			String name = input.nextLine();
			String artist = input.nextLine();
			String album = input.nextLine();
			String year = input.nextLine();
			SongElement oldSong = new SongElement(name, artist, album, year);
			obsList.add(oldSong);
		}
		
		Collections.sort(obsList, SongElement.Comparators.NAME);
		listView.setItems(obsList);
		input.close();
		
		listView.getSelectionModel().select(0);
		displaySelected();
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SongElement>()
		{
			@Override
			public void changed(ObservableValue<? extends SongElement> obs, SongElement oldValue, SongElement newValue) {
				displaySelected();
			}
		});
		
		addSong(null);
		editSong(null);
		deleteSong(null);
	}
	
	private void displaySelected()
	{
		SongElement selected = listView.getSelectionModel().getSelectedItem();
		if(selected != null)
		{
			songDisplay.setText(selected.name);
			artistDisplay.setText(selected.artist);
			albumDisplay.setText(selected.album);
			yearDisplay.setText(selected.year);
		}
		else
		{
			songDisplay.setText("--");
			artistDisplay.setText("--");
			albumDisplay.setText("--");
			yearDisplay.setText("--");
		}
	}
	
	public void addSong(ActionEvent e)
	{
		addSongButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) 
			{
				SongElement toAdd = new SongElement(songInput.getText(), artistInput.getText(), albumInput.getText(), yearInput.getText());
				
				//Format input--
				if(toAdd.year.isEmpty())
					toAdd.year = "--";
				else
					try
					{
						Integer.parseInt(toAdd.year);
					} catch(NumberFormatException e)
					{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Adding Song");
						alert.setHeaderText("Year input is not a valid number");
						alert.initOwner(mainStage);
						alert.showAndWait();
						return;
					}
				if(toAdd.album.isEmpty())
					toAdd.album = "--";
				
				//Evaluate input
				if(toAdd.name.isEmpty() || toAdd.artist.isEmpty())
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Adding Song");
					alert.setHeaderText("Song name input or Artist input must be filled in");
					alert.initOwner(mainStage);
					alert.showAndWait();
					return;
				} else if(obsList.contains(toAdd))
				{	
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Adding Song");
					alert.setHeaderText("This song has already been inputed");
					alert.initOwner(mainStage);
					alert.showAndWait();
					return;
				} else
				{
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Adding Song");
					alert.setHeaderText("Are you sure you want to add this song?");
					alert.setContentText("Song: " + toAdd.name + "\nArtist: " + toAdd.artist + "\nAlbum: " + toAdd.album + "\nYear: " + toAdd.year);
					alert.initOwner(mainStage);
					Optional<ButtonType> result = alert.showAndWait();
					if(result.isPresent() && result.get() == ButtonType.CANCEL)
						return;
					
					obsList.add(toAdd);
					try(
						FileWriter fw = new FileWriter("songList.txt", true);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter out = new PrintWriter(bw))
					{
						out.println();
						out.println(toAdd.name);
						out.println(toAdd.artist);
						out.println(toAdd.album);
						out.println(toAdd.year);
					}catch(IOException e)
					{
						Alert alert2 = new Alert(AlertType.ERROR);
						alert2.setTitle("Error Adding Song");
						alert2.setHeaderText("Error loading song into text file");
						alert2.initOwner(mainStage);
						alert2.showAndWait();
						return;
					}
				}
				Collections.sort(obsList, SongElement.Comparators.NAME);
				listView.setItems(obsList);
				listView.getSelectionModel().select(toAdd);
				
				displaySelected();
			}
		});//end .setOnAction
		
	}//end addSong
	
	public void editSong(ActionEvent e)
	{
		editSongButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) 
			{
				SongElement selectedSong = listView.getSelectionModel().getSelectedItem();
						
				boolean changes = false;
				
				String newName, newArtist, newAlbum, newYear;
				String nameChanges, artistChanges, albumChanges, yearChanges;
				
				if(!songInput.getText().isEmpty() && !songInput.getText().equals(selectedSong.name))
				{
					newName = songInput.getText();
					
					nameChanges = selectedSong.name + " -> " + newName;
					changes = true;
				}
				else
				{
					nameChanges = "No change";
					newName = selectedSong.name;
				}
				
				if(!artistInput.getText().isEmpty() && !artistInput.getText().equals(selectedSong.artist))
				{
					newArtist = artistInput.getText();
					
					artistChanges = selectedSong.artist + " -> " + newArtist;
					changes = true;
				}
				else
				{
					artistChanges = "No change";
					newArtist = selectedSong.artist;
				}
				
				if((!albumInput.getText().equals(selectedSong.album) && !(albumInput.getText().isEmpty() && selectedSong.album.equals("--"))))
				{
					newAlbum = albumInput.getText();
					if(newAlbum.isEmpty())
						newAlbum = "--";
					
					albumChanges = selectedSong.album + " -> " + newAlbum;
					changes = true;
				}
				else
				{
					albumChanges = "No change";
					newAlbum = selectedSong.album;
				}
				
				if(!yearInput.getText().equals(selectedSong.year) && !(yearInput.getText().isEmpty() && selectedSong.year.equals("--")))
				{
					if(yearInput.getText().equals(""))
						newYear = "--";
					else
					{
						try
						{
							Integer.parseInt(yearInput.getText());
							newYear = yearInput.getText();
							
							changes = true;
						} catch(NumberFormatException e)
						{
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error Editing Song");
							alert.setHeaderText("Year input is not a valid number");
							alert.initOwner(mainStage);
							alert.showAndWait();
							return;
						}
					}
					yearChanges = selectedSong.year + " -> " + newYear;
				}
				else
				{
					yearChanges = "No change";
					newYear = selectedSong.year;
				}
				
				if(!changes)
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Editing Song");
					alert.setHeaderText("Song has no changes");
					alert.initOwner(mainStage);
					alert.showAndWait();
					return; 
				}
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Editing Song");
				alert.setHeaderText("Are you sure you want to edit this song?");
				alert.setContentText("Song: " + nameChanges + "\nArtist: " + artistChanges + "\nAlbum: " + albumChanges + "\nYear: " + yearChanges);
				alert.initOwner(mainStage);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.isPresent() && result.get() == ButtonType.CANCEL)
					return;
				
				obsList.remove(selectedSong);
				SongElement toAdd = new SongElement(newName, newArtist, newAlbum, newYear); 
				obsList.add(toAdd);
				
				Collections.sort(obsList, SongElement.Comparators.NAME);
				listView.refresh();
				listView.getSelectionModel().select(toAdd);
				
				songInput.setText("");
				artistInput.setText("");
				albumInput.setText("");
				yearInput.setText("");
				
				displaySelected();
			}
		});
	}
	
	public void deleteSong(ActionEvent e)
	{
		deleteSongButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) 
			{
				SongElement selectedSong = listView.getSelectionModel().getSelectedItem();
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Deleting Song");
				alert.setHeaderText("Are you sure you want to delete this song?");
				alert.setContentText("Song: " + selectedSong.name + "\nArtist: " + selectedSong.artist + "\nAlbum: " + selectedSong.album + "\nYear: " + selectedSong.year);
				alert.initOwner(mainStage);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.isPresent() && result.get() == ButtonType.CANCEL)
					return;
				
				int deletedIndex = listView.getSelectionModel().getSelectedIndex();
				obsList.remove(selectedSong);
				listView.refresh();
				
				listView.getSelectionModel().select(deletedIndex);
				if(listView.getSelectionModel().getSelectedItem() == null)
				{
					listView.getSelectionModel().select(deletedIndex - 1);
				}
				
				displaySelected();
			}
		});
	}
}
