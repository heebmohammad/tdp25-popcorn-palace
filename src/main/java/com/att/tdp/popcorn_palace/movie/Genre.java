package com.att.tdp.popcorn_palace.movie;

public enum Genre {
    ACTION,
    DRAMA,
    COMEDY,
    THRILLER,
    HORROR,
    SCI_FI,
    ROMANCE,
    DOCUMENTARY,
    ANIMATION,
    FANTASY;

    public static Genre fromString(String genre) {
        switch (genre.toUpperCase()) {
            case "ACTION": return ACTION;
            case "DRAMA": return DRAMA;
            case "COMEDY": return COMEDY;
            case "THRILLER": return THRILLER;
            case "HORROR": return HORROR;
            case "SCI-FI": return SCI_FI;
            case "ROMANCE": return ROMANCE;
            case "DOCUMENTARY": return DOCUMENTARY;
            case "ANIMATION": return ANIMATION;
            case "FANTASY": return FANTASY;
            default: throw new IllegalArgumentException("Unknown genre: " + genre);
        }
    }
}
