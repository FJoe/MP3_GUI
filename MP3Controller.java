package view;

import java.util.Collections;
import java.util.Scanner;
import java.io.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import application.SongElement;
import application.AVLTree;

public class MP3Controller 
{
	@FXML TextField songInput;
	@FXML TextField  artistInput;
	@FXML TextField albumInput;
	@FXML TextField yearInput;
	@FXML Button addSongButton;
	@FXML ListView<SongElement> listView;
		
	private AVLTree list = new AVLTree();
	private ObservableList<SongElement> obsList;
	
	public void start()
	{
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
			list.insert(name, artist, album, year);
			SongElement oldSong = new SongElement(name, artist, album, year);
			obsList.add(oldSong);
		}
		
		Collections.sort(obsList, SongElement.Comparators.NAME);
		listView.setItems(obsList);
		input.close();
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
						//POP-UP ERROR FOR YEAR NOT BEING INTEGER
						;
					}
				if(toAdd.album.isEmpty())
					toAdd.album = "--";
				
				//Evaluate input
				if(toAdd.name.isEmpty() || toAdd.artist.isEmpty())
				{
					//POP-UP ERROR FOR MISSING SONG/ARTIST
					return;
				} else if(list.search(toAdd.name, toAdd.artist))
				{	
					//POP-UP ERROR FOR DUPLICATE SONG
					return;
				} else
				{
					//POP UP CONFIRMATION TO ADD SONG
					obsList.add(toAdd);
					list.insert(toAdd.name, toAdd.artist, toAdd.album, toAdd.year);
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
						//POP-UP ERROR, UNABLE TO LOAD SONG DATA TO TEXT FILE
					}
				}
				Collections.sort(obsList, SongElement.Comparators.NAME);
				listView.setItems(obsList);
			}
		});//end .setOnAction
	}//end addSong
}