package me.Josh123likeme.SpotifyDataAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
	
	public static void main(String[] args) {
		
		File folder = new File(System.getProperty("user.dir"));
		
		List<File> files = new ArrayList<File>();
		
		for (File file : folder.listFiles()) {
			
			if (file.isDirectory()) continue;
			
			if (file.getName().matches("Streaming_History_Audio_.*.json")) {
				
				System.out.println("found: " + file.getName());
				
				files.add(file);
				
			}
			
		}

		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>(); 
		
		for (int i = 0; i < files.size(); i++) {
			
			try {
				
				data.addAll(getData(files.get(i)));	
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Track[] allTracks = compileAllTrackData(data);
		Artist[] allArtists = compileAllArtistData(allTracks);
		
		System.out.println("\n\n~~~GENERAL STATS~~~~\n"
				+ "TOTAL TRACKS: " + allTracks.length + "\n"
				+ "TOTAL ARTISTS: " + allArtists.length + "\n"
				+ "TOTAL LISTENING TIME: " + (getTotalListeningTime(allTracks) / 3600000) + " hrs\n"
				+ "TOTAL LISTENS: " + getTotalListens(allTracks));
		
		//data analysis starts here
		
		System.out.println("\n~~~~MOST SONG LISTENING TIME~~~~");
		
		Track[] sortedBySongListeningTime = sortByTrackListeningTime(allTracks);
		
		for (int i = 0; i < 100; i++) {
			
			Track t = sortedBySongListeningTime[i];
			
			System.out.println((i + 1) + ") " + t.trackName + " by " + t.artistName + " (" + (t.msPlayed / 60000) + " mins)");
			
		}
		
		System.out.println("\n~~~~MOST SONG LISTENS~~~~");
		
		Track[] sortedBySongListens = sortByTrackListens(allTracks);
		
		for (int i = 0; i < 100; i++) {
			
			Track t = sortedBySongListens[i];
			
			System.out.println((i + 1) + ") " + t.trackName + " by " + t.artistName + " (" + t.timesPlayed + " times)");
			
		}
		
		System.out.println("\n~~~~MOST ARTIST LISTENING TIME~~~~");
		
		Artist[] sortedByArtistListeningTime = sortByArtistListeningTime(allArtists);
		
		for (int i = 0; i < 100; i++) {
			
			Artist a = sortedByArtistListeningTime[i];
			
			System.out.println((i + 1) + ") " + a.artistName + " (" + (a.msPlayed / 60000) + " mins)");
			
		}
		
		System.out.println("\n~~~~MOST ARTIST LISTENS~~~~");
		
		Artist[] sortedByArtistListens = sortByArtistListens(allArtists);
		
		for (int i = 0; i < 100; i++) {
			
			Artist a = sortedByArtistListens[i];
			
			System.out.println((i + 1) + ") " + a.artistName + " (" + a.timesPlayed + " times)");
			
		}
		
	}
	
	public static long getTotalListeningTime(Track[] tracks) {
		
		long total = 0;
		
		for (Track track : tracks) {
			
			total += track.msPlayed;
			
		}
		
		return total;
		
	}
	
	public static int getTotalListens(Track[] tracks) {
		
		int total = 0;
		
		for (Track track : tracks) {
			
			total += track.timesPlayed;
			
		}
		
		return total;
		
	}
	
	public static Track[] sortByTrackListeningTime(Track[] tracks) {
		
		Track[] sortedTracks = new Track[tracks.length];
		
		boolean[] alreadyRemoved = new boolean[tracks.length];
		
		for (int i = 0; i < sortedTracks.length; i++) {
			
			int highestTrackIndex = -1;
			long highestMsPlayed = -1;
			
			for (int j = 0; j < sortedTracks.length; j++) {
				
				if (tracks[j].msPlayed > highestMsPlayed && !alreadyRemoved[j]) {
					highestTrackIndex = j;
					highestMsPlayed = tracks[j].msPlayed;
				}
				
			}
			
			if (highestMsPlayed == -1) System.out.println("COULDNT FIND A SONG OVER -1 MS (THATS BAD)");
			
			sortedTracks[i] = tracks[highestTrackIndex];
			
			alreadyRemoved[highestTrackIndex] = true;
			
		}
		
		return sortedTracks;
			
	}
	
	public static Track[] sortByTrackListens(Track[] tracks) {
		
		Track[] sortedTracks = new Track[tracks.length];
		
		boolean[] alreadyRemoved = new boolean[tracks.length];
		
		for (int i = 0; i < sortedTracks.length; i++) {
			
			int highestTrackIndex = -1;
			long highestTimesPlayed = -1;
			
			for (int j = 0; j < sortedTracks.length; j++) {
				
				if (tracks[j].timesPlayed > highestTimesPlayed && !alreadyRemoved[j]) {
					highestTrackIndex = j;
					highestTimesPlayed = tracks[j].timesPlayed;
				}
				
			}
			
			if (highestTimesPlayed == -1) System.out.println("COULDNT FIND A SONG LISTENED TO OVER -1 TIMES (THATS BAD (IDEK HOW THAT HAPPENS))");
			
			sortedTracks[i] = tracks[highestTrackIndex];
			
			alreadyRemoved[highestTrackIndex] = true;
			
		}
		
		return sortedTracks;
			
	}
	
	public static Artist[] sortByArtistListeningTime(Artist[] artists) {

		Artist[] sortedArtists = new Artist[artists.length];
		
		boolean[] alreadyRemoved = new boolean[artists.length];
		
		for (int i = 0; i < sortedArtists.length; i++) {
			
			int highestArtistIndex = -1;
			long highestMsPlayed = -1;
			
			for (int j = 0; j < sortedArtists.length; j++) {
				
				if (artists[j].msPlayed > highestMsPlayed && !alreadyRemoved[j]) {
					highestArtistIndex = j;
					highestMsPlayed = artists[j].msPlayed;
				}
				
			}
			
			if (highestMsPlayed == -1) System.out.println("COULDNT FIND AN ARTIST WITH A LISTENING TIME OVER -1 MS (THATS BAD)");
			
			sortedArtists[i] = artists[highestArtistIndex];
			
			alreadyRemoved[highestArtistIndex] = true;
			
		}
		
		return sortedArtists;
			
	}
	
	public static Artist[] sortByArtistListens(Artist[] artists) {

		Artist[] sortedArtists = new Artist[artists.length];
		
		boolean[] alreadyRemoved = new boolean[artists.length];
		
		for (int i = 0; i < sortedArtists.length; i++) {
			
			int highestArtistIndex = -1;
			long highestTimesPlayed = -1;
			
			for (int j = 0; j < sortedArtists.length; j++) {
				
				if (artists[j].timesPlayed > highestTimesPlayed && !alreadyRemoved[j]) {
					highestArtistIndex = j;
					highestTimesPlayed = artists[j].timesPlayed;
				}
				
			}
			
			if (highestTimesPlayed == -1) System.out.println("COULDNT FIND AN ARTIST WITH OVER -1 LISTENS (THATS BAD (AND SHOULD BE IMPOSSIBLE (OBVIOUSLY)))");
			
			sortedArtists[i] = artists[highestArtistIndex];
			
			alreadyRemoved[highestArtistIndex] = true;
			
		}
		
		return sortedArtists;
			
	}
	
	public static Track[] compileAllTrackData(List<HashMap<String, String>> data) {
		
		List<Track> allTracks = new ArrayList<Track>();
		
		nextTrack:
		for (HashMap<String, String> trackEntry : data) {
			
			String trackUri = trackEntry.get("spotify_track_uri");
			long msPlayed = Long.parseLong(trackEntry.get("ms_played"));
			
			//search for duplicate entry
			for (int i = 0; i < allTracks.size(); i++) {
				
				//entry already exists, so just add the playtime
				if (trackUri.equals(allTracks.get(i).uri)) {
					
					allTracks.get(i).msPlayed += msPlayed;
					
					allTracks.get(i).timesPlayed++;
					
					continue nextTrack;
					
				}
				
			}
			
			//make a new song entry
			
			Track track = new Track();
			
			track.uri = trackUri;
			track.msPlayed = msPlayed;
			track.trackName = trackEntry.get("master_metadata_track_name");
			track.albumName = trackEntry.get("master_metadata_album_album_name");
			track.artistName = trackEntry.get("master_metadata_album_artist_name");
			
			track.timesPlayed = 1;
			
			allTracks.add(track);
			
		}
		
		Track[] allTracksArray = new Track[allTracks.size()];
		
		for (int i = 0; i < allTracks.size(); i++) {
			
			allTracksArray[i] = allTracks.get(i);
			
		}
		
		return allTracksArray;
		
	}
	
	public static Artist[] compileAllArtistData(List<HashMap<String, String>> data) {
		
		return compileAllArtistData(compileAllTrackData(data));
		
	}
	
	public static Artist[] compileAllArtistData(Track[] tracks) {
		
		List<Artist> allArtists = new ArrayList<Artist>();
		
		nextTrack:
		for (Track track : tracks) {
			
			//check if this artist is already registered
			for (int i = 0; i < allArtists.size(); i++) {
				
				if (track.artistName.equals(allArtists.get(i).artistName)) {
					
					allArtists.get(i).msPlayed += track.msPlayed;
					allArtists.get(i).timesPlayed += track.timesPlayed;
					
					continue nextTrack;
					
				}
				
			}
			
			//make a new artist entry
			
			Artist artist = new Artist();
			
			artist.artistName = track.artistName;
			artist.msPlayed = track.msPlayed;
			artist.timesPlayed = track.timesPlayed;
			
			allArtists.add(artist);
			
		}
		
		Artist[] allArtistsArray = new Artist[allArtists.size()];
		
		for (int i = 0; i < allArtists.size(); i++) {
			
			allArtistsArray[i] = allArtists.get(i);
			
		}
		
		return allArtistsArray;
		
	}
	
	public static List<HashMap<String, String>> getData(File file) throws FileNotFoundException, IOException {
		
		//get all lines
		
		List<String> linesArray = new ArrayList<String>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		       
		    	linesArray.add(line);
		    	
		    }
		    
		}	
		
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		
		//flag for if currently modifying a hashmap
		boolean currentlyMakingHashMap = false;
		
		HashMap<String, String> currentHashMap = null;
		
		for (String line : linesArray) {
			
			//valid line
			if (line.matches("\s*\"[a-z_]+\": .+,")) {
				
				if (!currentlyMakingHashMap) {
					
					currentHashMap = new HashMap<String, String>();
					data.add(currentHashMap);
					
					currentlyMakingHashMap = true;
				}
				
				line = line.strip();
				
				int line1End = line.indexOf('"', 1);
				
				String key = line.substring(1, line1End);	
				
				String value = line.substring(line1End + 3, line.length() - 1);
				
				if (value.charAt(0) == '"') value = value.substring(1);
				if (value.charAt(value.length() - 1) == '"') value = value.substring(0, value.length() - 1);
				
				currentHashMap.put(key, value);
				
			}
			else {
				
				currentlyMakingHashMap = false;
				
			}
			
		}
		
		return data;
		
	}
	
}
