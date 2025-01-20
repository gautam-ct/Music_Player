package in.clear.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(DemoApplication.class, args);
		SongManager system = new SongManager();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		system.addSong("Song A", "Artist 1", sdf.parse("2025-01-16"));
		system.addSong("Song B", "Artist 1", sdf.parse("2025-01-16"));
		system.addSong("Song C", "Artist 2", sdf.parse("2025-01-15"));
		system.addSong("Song D", "Artist 2", sdf.parse("2025-01-15"));
		system.play("Song A", "Artist 1");
		system.play("Song A", "Artist 1");
		system.play("Song A", "Artist 1");
		system.play("Song B", "Artist 1");
		System.out.println("\nSongs by Artist 1:");
		system.getSongsByArtist("Artist 1").forEach(System.out::println);
		System.out.println("\nTop 10 songs of 2025-01-17:");
		system.topSongsOfDay(sdf.parse("2025-01-17")).forEach(System.out::println);
		System.out.println("\nTop 10 songs of Artist 1:");
		system.topSongsByArtist("Artist 1").forEach(System.out::println);
		System.out.println("\nSongs played less than 2 times:");
		system.songsPlayLessThan(2).forEach(System.out::println);
		System.out.println("\nPlaylist for Artist 1:");
		system.getPlaylist("Artist 1").forEach(System.out::println);

	}
}
/*
	class Songs{
		String song_name;
		int id;
		int play_cnt;
		String artist_name;
	};
	class Artist{
		String artist_name;
		Priority_Queue<Songs>get_top_songs; // size up to 10;
	};
	Map<Songs ,

 */