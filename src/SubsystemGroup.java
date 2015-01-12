import java.util.ArrayList;
import java.util.List;

/**
 * Created by echo on 05.01.2015.
 * Группа подсистем. Содержит в себе список подсистем для установки и свойства установки группы
 */
public class SubsystemGroup {

    /**
     * Имя группы
     */
    private String name;
    /**
     * Заметка для группы
     */
    private String note;
    /**
     * Список подсистем в группе
     */
    private List<Subsystem> subsystemList;

    /**
     * Конструктор со всеми полями
     * @param name Имя группы
     * @param note Заметка для группы
     */
    public SubsystemGroup (String name, String note) {
        this.name = name;
        this.note = note;
        subsystemList = new ArrayList<Subsystem>();
    }

    /**
     * Добавим подсистему в список подсистем группы. Добавляем в конец списка.
     * @param subs Подсистема
     */
    public void addSubsystem ( Subsystem subs){
        subsystemList.add(subs);
    }

    /**
     * Добавим подсистему в список подсистем группы. Добавляем в указанный элемент списка.
     * @param subs Подсистема
     * @param index Индекс, под которым сохраним подсистему.
     */
    public void addSubsystem ( Subsystem subs, int index){
        subsystemList.add(index,subs);
    }

    /*------------------ Геттеры и Сеттеры --------------------*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Subsystem> getSubsystemList() {
        return subsystemList;
    }

    public void setSubsystemList(List<Subsystem> subsystemList) {
        this.subsystemList = subsystemList;
    }

}
