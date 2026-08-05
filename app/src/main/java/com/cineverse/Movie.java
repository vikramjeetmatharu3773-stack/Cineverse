package com.cineverse.app;

public class Movie {
    private int id;
    private String title;
    private String description;
    private String posterUrl;
    private String videoUrl;
    private int year;
    private int tmdbId;

    // Constructors
    public Movie() {}

    public Movie(int id, String title, String description, String posterUrl, String videoUrl, int year, int tmdbId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.videoUrl = videoUrl;
        this.year = year;
        this.tmdbId = tmdbId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getTmdbId() { return tmdbId; }
    public void setTmdbId(int tmdbId) { this.tmdbId = tmdbId; }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                '}';
    }
}