package games_library;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Library {
    @XmlElementWrapper(name = "games")
    @XmlElement(name = "game")
    private List<Game> games;
    @XmlAttribute(name = "producer")
    private Double totalLibraryPrice = 0d;

    public Library() {
    }

    public Library(List<Game> games, Double totalLibraryPrice) {
        this.games = games;
        this.totalLibraryPrice = totalLibraryPrice;

        games.sort((a, b) -> a.getPrice() < b.getPrice()
                ? -1 : a.getPrice().equals(b.getPrice()) ? 0 : 1);
    }

    public Library(List<Game> games) {
        this.games = games;
        for (Game game : games)
            totalLibraryPrice += game.getPrice();

        games.sort((a, b) -> a.getPrice() < b.getPrice()
                ? -1 : a.getPrice().equals(b.getPrice()) ? 0 : 1);
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
        totalLibraryPrice = 0d;
        for (Game game : games)
            totalLibraryPrice += game.getPrice();

        games.sort((a, b) -> a.getPrice() < b.getPrice()
                ? -1 : a.getPrice().equals(b.getPrice()) ? 0 : 1);
    }

    public Double getTotalLibraryPrice() {
        return totalLibraryPrice;
    }

    public void addGame(Game game) {
        if (games == null)
            games = new ArrayList<>();
        games.add(game);
        totalLibraryPrice += game.getPrice();

        games.sort((a, b) -> a.getPrice() < b.getPrice()
                ? -1 : a.getPrice().equals(b.getPrice()) ? 0 : 1);
    }

    public void deleteGame(Game game) {
        totalLibraryPrice -= game.getPrice();
        games.remove(game);
    }

    @Override
    public String toString() {
        return "Library{" +
                "games=" + games +
                '}';
    }
}
