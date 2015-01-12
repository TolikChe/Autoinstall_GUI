import javax.swing.*;

/**
 * Created by echo on 06.01.2015.
 */
public class Main {
    public static void main(String[] args) {
        //
        // Прочитаем конфиг из файла. Этот конфиг в качестве параметра передадим в класс, строящий форму.
        try {
            Config c = new Config("autoinstall_config.xml");
            c.readFromFile();
            new MainForm( c );
        } catch ( Exception ex ) {

        }



    }
}
