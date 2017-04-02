package org.gruppe2.game;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.gruppe2.Resources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PokerLog {

    private Document doc;
    private Element hand; // The Root element.
    private Element round;
    private Integer roundNumber;

    public PokerLog() {
        // Creates the doc.
        this.doc = newDoc();
        // Time of log's creation.
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        hand = doc.createElement("hand");
        hand.setAttribute("date", date);
        hand.setAttribute("time", time);
        doc.appendChild(hand);
        // Round Element
        round = doc.createElement("round");
        roundNumber = 0;
        round.setAttribute("number", roundNumber.toString());
        hand.appendChild(round);
    }

    public void incrementRound(List<Card> communityCards) {

        roundNumber++;
        round = doc.createElement("round");
        round.setAttribute("number", roundNumber.toString());
        round.setAttribute("community", communityCards.toString());
        hand.appendChild(round);
    }

    public void recordPlayerAction(Player player, RoundPlayer roundPlayer, Action action) {

        // Player Element and attributes related to it.
        Element playerElement = doc.createElement("player");
        playerElement.setAttribute("name", player.getName());
        playerElement.setAttribute("uuid", player.getUUID().toString());

        // Action Element, child of Player.
        Element actionElement = doc.createElement("action");
        actionElement.setAttribute("type", action.toString());
        if (action instanceof Action.Raise)
            actionElement.setAttribute("value", String.valueOf(((Action.Raise) action).getAmount()));
        playerElement.appendChild(actionElement);

        // Hole Cards element, child of Player.
        Element holeCardsElement = doc.createElement("cards");
        Card[] hole = roundPlayer.getCards();
        holeCardsElement.setAttribute("first", hole[0].toString());
        holeCardsElement.setAttribute("second", hole[1].toString());
        playerElement.appendChild(holeCardsElement);

        // Adds the finished player element to the active round parent.
        round.appendChild(playerElement);
    }

    public void writeToFile() {

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_DDD-HH_mm"));

        File logDir = new File(Resources.getUserDir() + File.separator + "PokerLog");
        if (!logDir.exists()) logDir.mkdirs();
        StreamResult outputFile = new StreamResult(logDir + File.separator + dateTime + ".xml");

        DOMSource source = new DOMSource(doc);

        try { transformer().transform(source, outputFile); }
        catch (TransformerException e) { e.printStackTrace(); }
    }

    private Transformer transformer() {

        Transformer transformer = null;
        try { transformer = TransformerFactory.newInstance().newTransformer(); }
        catch (TransformerConfigurationException e) { e.printStackTrace(); }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }

    private Document newDoc() {

        DocumentBuilder docBuild = null;
        try { docBuild = DocumentBuilderFactory.newInstance().newDocumentBuilder(); }
        catch (ParserConfigurationException e) { e.printStackTrace(); }
        return docBuild.newDocument();
    }

}
