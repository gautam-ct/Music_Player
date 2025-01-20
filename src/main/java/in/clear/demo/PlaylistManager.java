package in.clear.demo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PlaylistManager {
    private final Map<String, List<Song>> playlists;

    public PlaylistManager() {
        playlists = new HashMap<>();
    }

    // Add song to the artist's playlist
    public void addSongToPlaylist(String artist, Song song) {
        playlists.putIfAbsent(artist, new ArrayList<>());
        playlists.get(artist).add(song);

        // Export playlist to a CSV file whenever a new song is added
        exportPlaylistToCSV(artist);
    }

    // Get playlist for an artist
    public List<Song> getPlaylist(String artist) {
        return playlists.getOrDefault(artist, Collections.emptyList());
    }

    // Export the artist's playlist to a CSV file
    private void exportPlaylistToCSV(String artist) {
        String fileName = artist.replaceAll("\\s+", "_") + "_playlist.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Here is the listed song for artist: " + artist + "\n");
            for (Song song : playlists.get(artist)) {
                writer.append(song.name).append("\n");
            }
            System.out.println("Playlist for " + artist + " exported to " + fileName);
        } catch (IOException e) {
            System.err.println("Error exporting playlist for " + artist + ": " + e.getMessage());
        }
    }
}
