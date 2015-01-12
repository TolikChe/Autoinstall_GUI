/**
 * Created by echo on 05.01.2015.
 * Подсистема для установки. Содержит в себе свойства подсистемы
 */
public class Subsystem {

    /**
     * Порядок подсистеме в группе
     */
    private int order;
    /**
     * Имя подсистемы
     */
    private String name;
    /**
     * Источник файлов для подсистемы
     */
    private String source;
    /**
     * Место назначения файлов подсистемы
     */
    private String destination;
    /**
     * Опции
     */
    private String options;

    /**
     * Конструктор со всеми полями
     * @param order Порядок подсистеме в группе
     * @param name Имя подсистемы
     * @param source Источник файлов для подсистемы
     * @param destination Место назначения файлов подсистемы
     * @param options Опции
     */
    public Subsystem (int order, String name, String source, String destination, String options ) {
        this.order = order;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.options = options;
    }


    /*------------------ Геттеры и Сеттеры --------------------*/
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
