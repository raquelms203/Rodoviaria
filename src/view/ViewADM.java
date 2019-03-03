package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.TabelaCompras;
import controller.TabelaFuncionarios;
import controller.TabelaPassagens;
import controller.TabelaPoltronas;
import model.SqliteConnection;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * Frame do login 'adm'.
 * 
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
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
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
	 * Fecha a Frame.
	 */
	public void close_frame() {
		this.dispose();
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

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(938, 5, 24, 24);
		contentPane.add(menuBar);

		Image imgoff = new ImageIcon(this.getClass().getResource("/off.png")).getImage();
		JMenu menu = new JMenu("\"\"");
		menu.setOpaque(true);
		menu.setBackground(new Color(220, 220, 220));
		menu.setIcon(new ImageIcon(imgoff));
		menuBar.add(menu);

		ViewBilheteria vb = new ViewBilheteria(adm);
		JMenuItem menuEnc = new JMenuItem("Encerrar Passagens");
		menuEnc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vb.limpar_poltronas();
			}
		});
		menu.add(menuEnc);

		JMenuItem menuTrocar = new JMenuItem("Trocar Usuário");
		menuTrocar.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent e) {
				close_frame();
				Login l = new Login();
				l.main(null);
			}
		});
		menu.add(menuTrocar);

		Image imgsair = new ImageIcon(this.getClass().getResource("/sair.png")).getImage();

		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.setIcon(new ImageIcon(imgsair));
		menuSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		menu.add(menuSair);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(2, 8, 1097, 461);
		contentPane.add(tabbedPane);

		JTextField txtData = new JTextField();
		txtData = vb.clock();
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
		painelFuncionarios = (JPanel) tf.getContentPane();
		tabbedPane.addTab("Funcion\u00E1rios", null, painelFuncionarios, null);

		painelFuncionarios.setLayout(null);

		JPanel painelPassagens = new JPanel();
		TabelaPassagens tp = new TabelaPassagens();
		painelPassagens = (JPanel) tp.getContentPane();
		tabbedPane.addTab("Passagens", null, painelPassagens, null);

		JPanel painelBilheteria = new JPanel();

		vb.getTxtHorario().setVisible(false);
		vb.menuBar.setVisible(false);
		painelBilheteria = (JPanel) vb.getContentPane();
		tabbedPane.addTab("Bilheteria", null, painelBilheteria, null);
	}
}
