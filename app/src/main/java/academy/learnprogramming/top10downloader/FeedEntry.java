package academy.learnprogramming.top10downloader;

public class FeedEntry {

    private String title;
    private String link;
    private String pubDate;
    private String description;
    private String imageURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String artist) {
        this.link = artist;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String relaseData) {
        this.pubDate = relaseData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String summary) {
        this.description = summary;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "title=" + title + '\n' +
                ", link=" + link + '\n' +
                ", pubData=" + pubDate + '\n' +
                ", descryption=" + description + '\n';
    }
}
