import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.rmi.server.ExportException;

/**
 * Created by echo on 06.01.2015.
 */
public class MainForm extends JFrame {
    private JPanel mainPanel;
    private JTree treeConfig;
    private JTextField subsystemGroupName;
    private JPanel subsystemGroupPanel;
    private JPanel subsystemPanel;
    private JTextField subsystemName;
    private JPanel configPanel;
    private JPanel cardPanel;
    private JTextField subsystemSource;
    private JTextField subsystemDestination;
    private JTextField subsystemOptions;
    private JTextField subsystemGroupNote;
    private JButton buttonStartExecution;
    private JButton buttonStopExecution;
    private JButton buttonReloadConfig;
    private JButton buttonSaveConfig;

    // Конфиг который прочитали из файла конфига
    private Config conf;

    public MainForm( final Config conf ) {

        // Запомним конфиг
        this.conf = conf;

        // Свойства формы
        setTitle("Autoinstall");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);

        //Установим карду из панели которая будет открыта на старте
        ((CardLayout)(cardPanel.getLayout())).show(cardPanel, "configCard");
        buttonStartExecution.setEnabled(false);
        buttonStopExecution.setEnabled(false);

        // Инициализация дерева
        initTree();

        // Сделаем главной панелью формы панель mainPanel
        setContentPane(mainPanel);
        // Сожмем размеры фомы до минимаьлно допусимых
        pack();
        // Покажем форму
        setVisible(true);

        /**
         * Обрабатываем переключения в дереве
         */
        treeConfig.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                // Получим текущий выделенный элемент
                TreePath currentSelection = treeConfig.getSelectionPath();
                // Если есть выделенный элемент
                if (currentSelection != null) {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                    switch (currentNode.getLevel()) {
                        case 0 :  buttonStartExecution.setEnabled(false);
                            buttonStopExecution.setEnabled(false);
                            ((CardLayout) (cardPanel.getLayout())).show(cardPanel, "configCard");
                            break;
                        case 1 :  buttonStartExecution.setEnabled(true);
                            buttonStopExecution.setEnabled(true);
                            initSubsystemGroupPanel(currentNode.getUserObject().toString());
                            ((CardLayout)(cardPanel.getLayout())).show(cardPanel, "subsystemGroupCard");
                            break;
                        case 2 :  buttonStartExecution.setEnabled(true);
                            buttonStopExecution.setEnabled(true);
                            initSubsystemPanel(currentNode.getUserObject().toString(), ((DefaultMutableTreeNode) currentNode.getParent()).getUserObject().toString());
                            ((CardLayout)(cardPanel.getLayout())).show(cardPanel, "subsystemCard");
                            break;
                    }

                    // JOptionPane.showMessageDialog(MainForm.this, "CurrentNode " + currentNode.getUserObject() + " -> " + currentNode.getLevel());
                }
            }
        });

        /**
         * Обрабатываем нажатие кнопки Обновить
         */
        buttonReloadConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    conf.readFromFile();
                    initTree();
                } catch ( Exception ex ) {
                    JOptionPane.showMessageDialog(MainForm.this, ex.getMessage());
                }
            }
        });

        /**
         * Обрабатываем нажатие кнопки Сохранить
         */
        buttonSaveConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получим текущий выделенный элемент дерева
                TreePath currentSelection = treeConfig.getSelectionPath();
                // Если есть выделенный элемент
                if (currentSelection != null) {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
                    switch (currentNode.getLevel()) {
                        // Изменим
                        case 1 :  List<SubsystemGroup> s_g_l = conf.getSubsystemGroupList();
                                  s_g_l.get(0)
                                  break;
                        case 2 :  buttonStartExecution.setEnabled(true);
                            buttonStopExecution.setEnabled(true);
                            initSubsystemPanel(currentNode.getUserObject().toString(), ((DefaultMutableTreeNode) currentNode.getParent()).getUserObject().toString());
                            ((CardLayout)(cardPanel.getLayout())).show(cardPanel, "subsystemCard");
                            break;
                    }

                    try {
                        conf.saveToFile();
                    } catch ( Exception ex ) {
                        JOptionPane.showMessageDialog(MainForm.this, ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Инициализация дерева каким либо образом
     */
    public void initTree(){

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Список конфигураций");

        for (SubsystemGroup s_g : conf.getSubsystemGroupList()){
            DefaultMutableTreeNode subsGroupNode = new DefaultMutableTreeNode( s_g.getName() );

            for (Subsystem s : s_g.getSubsystemList()) {
                subsGroupNode.add( new DefaultMutableTreeNode(s.getName()));
            }
            root.add(subsGroupNode);
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        treeConfig.setModel(treeModel);
    }

    /**
     * Инициализация панели с подсистемами по имени подсистемы  и имени группы подсистемы
     * @param subsName Имя подсистемы
     * @param subsGroupName Имя группы подсистемы
     */
    public void initSubsystemPanel (String subsName, String subsGroupName){
        Subsystem subs = conf.getSubsystemByName(subsName, subsGroupName);
        subsystemName.setText(subs.getName());
        subsystemSource.setText(subs.getSource());
        subsystemDestination.setText(subs.getDestination());
        subsystemOptions.setText(subs.getOptions());
    }

    /**
     * Инициализация панели с группой подсистемам по имени группы подсистемы
     * @param subsGroupName Имя группы подсистемы
     */
    public void initSubsystemGroupPanel (String subsGroupName){
        SubsystemGroup subsGroup = conf.getSubsystemGroupByName(subsGroupName);
        subsystemGroupName.setText(subsGroup.getName());
        subsystemGroupNote.setText(subsGroup.getNote());
    }



}
