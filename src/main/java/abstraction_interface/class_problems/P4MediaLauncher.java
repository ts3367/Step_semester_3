package abstraction_interface.class_problems;

// P4. Universal Media Launcher
// AudioFile IS-A MediaFile; Podcast is only CAN-DO Playable.
public class P4MediaLauncher {

    // Capability contract with an overloaded pair -> compile-time polymorphism.
    public interface Playable {
        String play();
        String play(int fromSecond);
        String pause();
    }

    // Abstract class for real media files: shared identity (final fileId from a
    // shared static counter) and a format contract.
    public abstract static class MediaFile {
        private static int fileCounter = 1000;

        private final String fileId;

        protected MediaFile() {
            fileCounter++;
            this.fileId = "MF-" + fileCounter;
        }

        public abstract String getFormatInfo();

        public String getFileId() {
            return fileId;
        }
    }

    // IS-A: an audio file genuinely is a media file - it shares the fileId and
    // the format contract, so it belongs in the MediaFile hierarchy.
    public static class AudioFile extends MediaFile implements Playable {
        private final String title;

        public AudioFile(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String play() {
            return "Playing audio: " + title;
        }

        @Override
        public String play(int fromSecond) {
            return "Playing audio: " + title + " from " + formatTime(fromSecond);
        }

        @Override
        public String pause() {
            return "Paused audio: " + title;
        }

        @Override
        public String getFormatInfo() {
            return "Audio file, ID: " + getFileId();
        }
    }

    // CAN-DO only: a podcast is streamed live, so it has no fileId and no
    // getFormatInfo() - forcing it into MediaFile's shape would give it nothing.
    public static class Podcast implements Playable {
        private final String showName;
        private final int episodeNumber;

        public Podcast(String showName, int episodeNumber) {
            this.showName = showName;
            this.episodeNumber = episodeNumber;
        }

        public String getShowName() {
            return showName;
        }

        public int getEpisodeNumber() {
            return episodeNumber;
        }

        @Override
        public String play() {
            return "Streaming episode " + episodeNumber + " of " + showName;
        }

        @Override
        public String play(int fromSecond) {
            return "Streaming episode " + episodeNumber + " of " + showName
                    + " from " + formatTime(fromSecond);
        }

        @Override
        public String pause() {
            return "Paused episode " + episodeNumber + " of " + showName;
        }
    }

    // 30 -> "0:30"
    public static String formatTime(int fromSecond) {
        int minutes = fromSecond / 60;
        int seconds = fromSecond % 60;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : String.valueOf(seconds));
    }

    // Loops a mixed array and calls the no-argument play() on each.
    public static void launchAll(Playable[] items) {
        if (items == null) return;
        for (Playable item : items) {
            if (item == null) continue;
            System.out.println(item.play());
        }
    }

    public static void main(String[] args) {
        AudioFile a = new AudioFile("Morning Jazz");
        System.out.println(a.play());        // Playing audio: Morning Jazz
        System.out.println(a.play(30));     // Playing audio: Morning Jazz from 0:30
        System.out.println(a.getFormatInfo()); // Audio file, ID: MF-1001

        Podcast p = new Podcast("Tech Talk", 12);
        System.out.println(p.play());        // Streaming episode 12 of Tech Talk

        // Upcasting: an AudioFile reference stored as the Playable interface type.
        Playable ref = a;
        launchAll(new Playable[]{ref, p});
    }
}
