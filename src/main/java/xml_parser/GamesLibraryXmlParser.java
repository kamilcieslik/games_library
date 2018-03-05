package xml_parser;

import games_library.Library;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class GamesLibraryXmlParser {
    private JAXBContext jaxbContext;

    public GamesLibraryXmlParser() {
    }

    public Library readGamesLibrary(File gamesLibraryFile) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(Library.class);
        Unmarshaller jaxbUnmarshaller;
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (Library) jaxbUnmarshaller.unmarshal(gamesLibraryFile);
    }

    public void writeGamesLibrary(Library gamesLibrary, String directoryPath, String fileName) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(Library.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File testTemplateFile = new File(directoryPath, fileName + ".xml");
        jaxbMarshaller.marshal(gamesLibrary, testTemplateFile);
    }
}
