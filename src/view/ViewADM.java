package view;


import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.TabelaCompras;
import controller.TabelaFuncionarios;
import controller.TabelaPassagens;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/** Frame do login 'adm'.
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class ViewADM extends JFrame {

	private JPanel contentPane;
	@SuppressWarnings("unused")
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |  javax.swing.UnsupportedLookAndFeelException ex) {
           System.err.println(ex);        	
        }

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewADM frame = new ViewADM("");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ViewADM(String adm) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1115, 508);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(2, 8, 1097, 461);
		contentPane.add(tabbedPane);
		
		ViewBilheteria cp = new ViewBilheteria(adm);
		JTextField txtData = new JTextField();
		txtData = cp.clock();
		txtData.setColumns(10);
		txtData.setBounds(975, 8, 120, 17);
		contentPane.add(txtData);
		
		JPanel painelHistorico = new JPanel();
		TabelaCompras tc = new TabelaCompras();
		painelHistorico = (JPanel) tc.getContentPane();
		tabbedPane.addTab("Hist\u00F3rico", null, painelHistorico, null);
		
		painelHistorico.setLayout(null);
		
		JPanel painelFuncionarios = new JPanel();
		TabelaFuncionarios tf = new TabelaFuncionarios();
		painelFuncionarios = (JPanel)tf.getContentPane();
		tabbedPane.addTab("Funcion\u00E1rios", null, painelFuncionarios, null);
		
		painelFuncionarios.setLayout(null);
		
		JPanel painelPassagens = new JPanel();
		TabelaPassagens tp = new TabelaPassagens();
		painelPassagens = (JPanel)tp.getContentPane();
		tabbedPane.addTab("Passagens", null, painelPassagens, null);
		
		JPanel painelBilheteria = new JPanel();
		
		cp.getTxtHorario().setVisible(false);
		painelBilheteria = (JPanel) cp.getContentPane();
		tabbedPane.addTab("Bilheteria", null, painelBilheteria, null);		
		
		Image imgoff = new ImageIcon(this.getClass().getResource("/off.png")).getImage();
		
		JButton btnOff = new JButton("");
		btnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnOff.setBounds(940, 4, 24, 24);
		btnOff.setIcon(new ImageIcon(imgoff));
		
		contentPane.add(btnOff);
	}
}
