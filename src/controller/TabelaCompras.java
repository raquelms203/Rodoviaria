package controller;


import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.CellRenderer;
import model.SqliteConnection;
import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class TabelaCompras extends JFrame {

	private JPanel contentPane;
	private JTextField txtPesquisar;
	@SuppressWarnings("rawtypes")
	private JComboBox comboPesquisar;
	private JTable tabelaCompras;
	private JButton btnAtualiza;

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
					TabelaCompras frame = new TabelaCompras();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Functions.
	 */

	/**
	 * Função para exibir a tabela do banco de dados com o Model DBUtils (Library rs2xml.jar) e formatação da 
	 * tabela com a classe CellRenderer. Ordena por ordem cronológica da compra.
	 */
	public void mostrarTabela() {
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "SELECT partida_cidade, destino_cidade, partida_horario, destino_horario, data, poltrona, cliente, caixa "
					+ "FROM compras ORDER BY data DESC";
			PreparedStatement prep = connec.prepareStatement(query);
			ResultSet rs = prep.executeQuery();
			tabelaCompras.setModel(DbUtils.resultSetToTableModel(rs));
			tabelaCompras.setDefaultRenderer(Object.class, new CellRenderer(tabelaCompras));
			rs.close();
			prep.close();
			connec.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	/** Função que procura o valor selecionado do combobox com o valor do parâmetro e exibe uma tabela apenas com os resultados.
	 * Para voltar, é necessário limpar um dos campos. Caso há algum campo vazio, 
	 * exibe um pop up com alerta.
	 * @param String a ser pesquisada
	 */
	public void pesquisarTabela(String input) {
		try {
			
			if (comboPesquisar.getSelectedItem().toString().compareTo("") == 0 || txtPesquisar.getText().compareTo("") == 0) {
				comboPesquisar.setSelectedIndex(0);
				txtPesquisar.setText("");
				mostrarTabela();
			}
			
			Connection connec = SqliteConnection.dbBilheteria();
			String query = ("SELECT * FROM compras WHERE " + comboPesquisar.getSelectedItem().toString() + "='" + input
					+ "'");
			PreparedStatement prep = connec.prepareStatement(query);
			ResultSet rs = prep.executeQuery();

			tabelaCompras.setModel(DbUtils.resultSetToTableModel(rs));
			tabelaCompras.setDefaultRenderer(Object.class, new CellRenderer(tabelaCompras));

			if (tabelaCompras.getRowCount() == 0) {
				comboPesquisar.setSelectedIndex(0);
				txtPesquisar.setText("");
				mostrarTabela();
			}
			rs.close();
			prep.close();
			connec.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	
	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TabelaCompras() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1079, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 41, 1043, 406);
		contentPane.add(scrollPane);
		
		tabelaCompras = new JTable() {
			private static final long serialVersionUID = 1L;
	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
		};
		
		tabelaCompras.setBackground(new Color(240, 230, 140));
		scrollPane.setViewportView(tabelaCompras);
		mostrarTabela();

		comboPesquisar = new JComboBox();
		comboPesquisar.setModel(new DefaultComboBoxModel(new String[] { "", "destino_cidade", "cliente", "caixa" }));
		comboPesquisar.setBounds(89, 11, 117, 20);
		contentPane.add(comboPesquisar);

		txtPesquisar = new JTextField();
		txtPesquisar.setBounds(221, 11, 173, 20);
		contentPane.add(txtPesquisar);
		txtPesquisar.setColumns(10);
		txtPesquisar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					pesquisarTabela(txtPesquisar.getText());
			}
		});

		JLabel lblPesquisarPor = new JLabel("Pesquisar por:");
		lblPesquisarPor.setBounds(12, 14, 89, 14);
		contentPane.add(lblPesquisarPor);

		Image imgsearch = new ImageIcon(this.getClass().getResource("/search.png")).getImage();

		JButton btnPesquisar = new JButton("");
		btnPesquisar.setToolTipText("Pesquisar");
		btnPesquisar.setBounds(404, 9, 24, 24);
		btnPesquisar.setIcon(new ImageIcon(imgsearch));
		contentPane.add(btnPesquisar);
		
		Image imgatualiza = new ImageIcon(this.getClass().getResource("/atualiza.png")).getImage();
		
		btnAtualiza = new JButton("");
		btnAtualiza.setToolTipText("Atualizar");
		btnAtualiza.setIcon(new ImageIcon(imgatualiza));
		btnAtualiza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarTabela();
			}
		});
		btnAtualiza.setBounds(436, 9, 24, 24);
		contentPane.add(btnAtualiza);
	}
}
