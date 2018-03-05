package games_library;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Game {
    @XmlAttribute(name = "title")
    private String title;
    @XmlAttribute(name = "producer")
    private String producer;
    @XmlAttribute(name = "publisher")
    private String publisher;
    @XmlAttribute(name = "releaseDate")
    private Date releaseDate;
    @XmlAttribute(name = "price")
    private Double price;
    @XmlAttribute(name = "imagePath")
    private String imagePath;

    public Game() {
    }

    public Game(String title, String producer, String publisher, Date releaseDate, Double price, String imagePath) {
        this.title = title;
        this.producer = producer;
        this.publisher = publisher;
        this.releaseDate = releaseDate;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getProducer() {
        return producer;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return "Game{" +
                "title='" + title + '\'' +
                ", producer='" + producer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", releaseDate=" + releaseDate +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
