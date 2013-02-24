import javax.swing.JFrame;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    /** Creates new form MainFrame */
    public MainFrame() {
        //  initComponents();
        this.validate();
        this.setResizable(false);
        this.setTitle("Traffic");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(new MainPanel());
        this.pack();
        this.setVisible(true);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

