import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by echo on 05.01.2015.
 */
public class Config {
    /**
     * Имя файла в котором храниться конфигурация
     */
    private String filename;

    /**
     * Список групп подсистем, с которыми выполняется работа.
     */
    private List<SubsystemGroup> subsystemGroupList;

    /**
     * Конструктор объекта.
     * @param filename Имя файла с конфигурацией.
     */
    public Config (String filename) {
        this.filename = filename;
    }

    /**
     * Сохраним значение объекта в файл
     */
    public void saveToFile() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Корень
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("CONFIG");
        doc.appendChild(rootElement);

        // Переберем группы подсистем
        for (SubsystemGroup s_g : subsystemGroupList){
            Element subsystemGroup = doc.createElement("SUBSYSTEM_GROUP");
            // Создадим тег NAME
            Element subsystemGroupName = doc.createElement("NAME");
            subsystemGroupName.appendChild(doc.createTextNode(s_g.getName()));
            // Создадим тег NOTE
            Element subsystemGroupNote = doc.createElement("NOTE");
            subsystemGroupNote.appendChild(doc.createTextNode(s_g.getNote()));
            // Внесем теги в документ
            subsystemGroup.appendChild(subsystemGroupName);
            subsystemGroup.appendChild(subsystemGroupNote);
            // Переберем список подсистем в группе
            for (Subsystem s : s_g.getSubsystemList()) {
                Element subsystem = doc.createElement("SUBSYSTEM");
                // Добавим аттрибут order
                subsystem.setAttribute("order", String.valueOf(s.getOrder()));
                // Создадим тег NAME
                Element subsystemName = doc.createElement("NAME");
                subsystemName.appendChild(doc.createTextNode(s.getName()));
                // Создадим тег SOURCE
                Element subsystemSource = doc.createElement("SOURCE");
                subsystemSource.appendChild(doc.createTextNode(s.getSource()));
                // Создадим тег DESTINATION
                Element subsystemDestination = doc.createElement("DESTINATION");
                subsystemDestination.appendChild(doc.createTextNode(s.getDestination()));
                // Создадим тег OPTIONS
                Element subsystemOptions = doc.createElement("OPTIONS");
                subsystemOptions.appendChild(doc.createTextNode(s.getOptions()));
                // Внесем теги в документ
                subsystem.appendChild(subsystemName);
                subsystem.appendChild(subsystemSource);
                subsystem.appendChild(subsystemDestination);
                subsystem.appendChild(subsystemOptions);
                //
                subsystemGroup.appendChild(subsystem);
            }
            rootElement.appendChild(subsystemGroup);
        }

        // Сохраним документ в файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filename));

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        System.out.println("File saved!");
    }

    /**
     * Прочиатаем значения в объект из файла
     */
    public void readFromFile() throws ParserConfigurationException, IOException, SAXException {
        // Наполним этот список групп подсистем
        subsystemGroupList = new ArrayList<SubsystemGroup>();

        File fXmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        System.out.println("Root element : " + doc.getDocumentElement().getNodeName());

        // Получим список групп подсистем
        NodeList subsystemGroupNodeList = doc.getElementsByTagName("SUBSYSTEM_GROUP");

        // Переберем список групп подсистем
        for (int i = 0; i < subsystemGroupNodeList.getLength(); i++) {
            // Это группа подсистем
            Element group = (Element)subsystemGroupNodeList.item(i);
            // Создадим объект "Группа подсистем"
            SubsystemGroup subsGroup = new SubsystemGroup(  group.getElementsByTagName("NAME").item(0).getTextContent(),
                                                            group.getElementsByTagName("NOTE").item(0).getTextContent() );



            // Получим список подсистем в группе
            NodeList subsystemNodeList = group.getElementsByTagName("SUBSYSTEM");
            // Переберем список подсистем в группе
            for (int j = 0; j < subsystemNodeList.getLength(); j++) {
                // Это подсистема
                Element subsystem = (Element)subsystemNodeList.item(j);
                int subsOrder = Integer.parseInt (subsystem.getAttribute("order"));

                // Добавим подсистему в группу
                subsGroup.addSubsystem(new Subsystem(subsOrder,
                                                        subsystem.getElementsByTagName("NAME").item(0).getTextContent(),
                                                        subsystem.getElementsByTagName("SOURCE").item(0).getTextContent(),
                                                        subsystem.getElementsByTagName("DESTINATION").item(0).getTextContent(),
                                                        subsystem.getElementsByTagName("OPTIONS").item(0).getTextContent()
                                                    ),
                                                    subsOrder);

            }

            // Добавим группу подсистем в список групп
            subsystemGroupList.add(subsGroup);
        }
        System.out.println("File read!");
    }

    /**
     * Получим подсистему по имени подсистемы и имени группы подсистемы
     * @param subsystemName Имя подсистемы
     * @param subsystemGroupName Имя группы подсистем
     * @return Подсистема с требуемым именем
     */
    public Subsystem getSubsystemByName(String subsystemName, String subsystemGroupName){
        Subsystem result = null;
        for (SubsystemGroup s_g : subsystemGroupList ){
            if (s_g.getName().equals(subsystemGroupName)){
                for (Subsystem s : s_g.getSubsystemList()){
                    if (s.getName().equals(subsystemName)){
                        return s;
                    }
                }
            }
        }
        return result;
    }


    /**
     * Получим группу подсистем по имени
     * @param subsystemGroupName Имя группы подсистем
     * @return Группа подсистем с требуемым именем
     */
    public SubsystemGroup getSubsystemGroupByName(String subsystemGroupName){
        SubsystemGroup result = null;
        for (SubsystemGroup s_g : subsystemGroupList ){
            if (s_g.getName().equals(subsystemGroupName)){
               return s_g;
            }
        }
        return result;
    }


    /**
     * Переведем содержимое объекта в строку
     */
    public String toString() {
        String result = "";

        for ( SubsystemGroup s_g  : subsystemGroupList ){
            result = result + "SUBSYSTEM_GROUP -> " + " name : " + s_g.getName() +  "; note : " + s_g.getNote() + "\n";

            for ( Subsystem s : s_g.getSubsystemList() ){
                result  = result + "    SUBSYSTEM -> " + " name :" + s.getName() + "; source : " + s.getSource() + "; destination : " + s.getDestination() + "; options : " + s.getOptions() + "; order : " + s.getOrder() + "\n";
            }
        }
        return result;
    }


    /*------------------ Геттеры и Сеттеры --------------------*/
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<SubsystemGroup> getSubsystemGroupList() {
        return subsystemGroupList;
    }

    public void setSubsystemGroupList(List<SubsystemGroup> subsystemGroupList) {
        this.subsystemGroupList = subsystemGroupList;
    }

}


