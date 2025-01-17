package in.clear.demo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
import java.util.*;
public class SongManager {
    private TreeMap<String, TreeMap<Pair<Integer, Integer>, Song>> artistMap;
    private TreeMap<String, TreeMap<Pair<Integer, Integer>, Song>> dateMap;
    private TreeMap<String, Integer> trackPlayCount;

    public SongManager() {
        artistMap = new TreeMap<>();
        dateMap = new TreeMap<>();
        trackPlayCount = new TreeMap<>();
    }

    public String Formatted_date(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public void addSong(String songName, String artist, Date date) {
        try {
            String releaseDate = Formatted_date(date);
            Song song = new Song(songName, artist);
            trackPlayCount.put(songName + "|" + artist, 0);
            artistMap.putIfAbsent(artist, new TreeMap<>((p1, p2) -> Integer.compare(p1.first, p2.first)));
            Pair<Integer, Integer> key = new Pair<>(0, songName.hashCode() + artist.hashCode());
            artistMap.get(artist).put(key, song);
            dateMap.putIfAbsent(releaseDate, new TreeMap<>((p1, p2) -> Integer.compare(p1.first, p2.first)));
            dateMap.get(releaseDate).put(key, song);
        } catch (Exception e) {
            System.err.println("Error adding song: " + e.getMessage());
        }
    }

    public void play(String songName, String artistName) {
        try {
            if (songName != null && artistName != null) {
                String uniqueId = songName + "|" + artistName;
                if (trackPlayCount.containsKey(uniqueId)) {
                    Song song = new Song(songName, artistName);
                    incrementPlayCount(song);
                }
            } else if (songName == null && artistName != null) {
                for (Map.Entry<String, Integer> entry : trackPlayCount.entrySet()) {
                    String uniqueId = entry.getKey();
                    String[] parts = uniqueId.split("\\|");
                    if (parts[1].equals(artistName)) {
                        Song song = new Song(parts[0], artistName);
                        incrementPlayCount(song);
                    }
                }
            } else if (artistName == null && songName != null) {
                for (Map.Entry<String, Integer> entry : trackPlayCount.entrySet()) {
                    String uniqueId = entry.getKey();
                    String[] parts = uniqueId.split("\\|");
                    if (parts[0].equals(songName)) {
                        Song song = new Song(songName, parts[1]);
                        incrementPlayCount(song);
                    }
                }
            } else {
                System.out.println("No song or artist found with the provided details.");
            }
        } catch (Exception e) {
            System.err.println("Error playing song: " + e.getMessage());
        }
    }

    private void incrementPlayCount(Song song) {  //on an average it will take log(n) time to insert the song in the treeMap
        try {
            String uniqueId = song.name + "|" + song.artist;
            int playCount = trackPlayCount.get(uniqueId) + 1;
            trackPlayCount.put(uniqueId, playCount);
            String artist = song.artist;
            if (!artistMap.containsKey(artist)) {
                artistMap.put(artist, new TreeMap<>((p1, p2) -> Integer.compare(p1.first, p2.first)));
                artistMap.get(artist).put(new Pair<>(playCount, (song.name + "|" + artist).hashCode()), song);
            } else {
                TreeMap<Pair<Integer, Integer>, Song> artistSongs = artistMap.get(artist);
                Pair<Integer, Integer> key = new Pair<>(playCount, (song.name + "|" + artist).hashCode());
                artistSongs.remove(new Pair<>(key.first - 1, key.second));
                artistSongs.put(key, song);
            }
            Date date = new Date();
            String today = Formatted_date(date);
            System.out.println("Today's Date: " + today);
            if (!dateMap.containsKey(today)) {
                dateMap.put(today, new TreeMap<>((p1, p2) -> Integer.compare(p1.first, p2.first)));
                dateMap.get(today).put(new Pair<>(playCount, (song.name + "|" + artist).hashCode()), song);
            } else {
                TreeMap<Pair<Integer, Integer>, Song> dateSongs = dateMap.get(today);
                Pair<Integer, Integer> key = new Pair<>(playCount, (song.name + "|" + artist).hashCode());
                dateSongs.remove(new Pair<>(key.first - 1, key.second));
                dateSongs.put(key, song);
            }
        } catch (Exception e) {
            System.err.println("Error incrementing play count: " + e.getMessage());
        }
    }

    public List<Song> getSongsByArtist(String artist) {
        try {
            if (!artistMap.containsKey(artist)) {
                return Collections.emptyList();
            }
            List<Song> result = new ArrayList<>();
            for (Map.Entry<Pair<Integer, Integer>, Song> entry : artistMap.get(artist).entrySet()) {
                result.add(entry.getValue());
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error retrieving songs by artist: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Song> topSongsOfDay(Date currDate) {
        try {
            String date = Formatted_date(currDate);
            if (!dateMap.containsKey(date)) {
                return Collections.emptyList();
            }
            List<Song> result = new ArrayList<>();
            for (Map.Entry<Pair<Integer, Integer>, Song> entry : dateMap.descendingMap().get(date).entrySet()) {

                result.add(entry.getValue());
                if (result.size() == 10) {
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error retrieving top songs of the day: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Song> topSongsByArtist(String artist) {
        try {
            if (!artistMap.containsKey(artist)) {
                return Collections.emptyList();
            }
            List<Song> result = new ArrayList<>();
            for (Map.Entry<Pair<Integer, Integer>, Song> entry : artistMap.descendingMap().get(artist).entrySet()) {
                result.add(entry.getValue());
                if (result.size() == 10) {
                    break;
                }
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error retrieving top songs by artist: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Song> songsPlayLessThan(int playCount) {
        List<Song> result = new ArrayList<>();
        try {
            for (Map.Entry<String, Integer> entry : trackPlayCount.entrySet()) {
                if (entry.getValue() < playCount) {
                    String[] parts = entry.getKey().split("\\|");
                    result.add(new Song(parts[0], parts[1]));
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving songs with less than " + playCount + " plays: " + e.getMessage());
        }
        return result;
    }
};