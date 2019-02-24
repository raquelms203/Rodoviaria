package controller;



import java.awt.EventQueue;
import java.awt.Image;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.CellRenderer;
import model.SqliteConnection;
import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class TabelaPassagens extends JFrame {

	private JPanel contentPane;
	private JTable tabelaPassagens;
	private JTextField txtDestino;
	
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
					TabelaPassagens frame = new TabelaPassagens();
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
	 * Mostra a tabela de passagens disponíveis com ligação no banco de dados.
	 */
	public void mostrarTabela() {
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "SELECT destino_cidade, partida_horario, destino_horario, preco FROM "
					+ "passagens ORDER BY destino_cidade ASC, destino_horario ASC ";
			PreparedStatement prep = connec.prepareStatement(query);
			ResultSet rs = prep.executeQuery();
			
			tabelaPassagens.setModel(DbUtils.resultSetToTableModel(rs));
			tabelaPassagens.setDefaultRenderer(Object.class, new CellRenderer(tabelaPassagens));
			rs.close();
			prep.close();
			connec.close();
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	
	/** Função para verificar se o parâmetro é a única cidade registrada na tabela 'passagens'
	 * @param cidade
	 * @return valor boolean
	 */
	public boolean unica_cidade (String cidade) {
		try {
			boolean flag;
			int i=0;
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "SELECT destino_cidade FROM passagens WHERE destino_cidade=?";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, cidade);
			ResultSet rs = prep.executeQuery();
			
			while (rs.next()) /// SE FOR A ÚNICA CIDADE, ESSE LAÇO SÓ EXECUTARÁ UMA VEZ
				i++;
			
			if (i==1)
				flag = true;
			  else
				flag = false;
			
			rs.close();
			prep.close();
			connec.close();		
			return flag;
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		return false;
	}
	
	
	/** Função para apagar a cidade da tabela 'cidades' se antes havia apenas um horário de ônibus para lá. 
	 * @param cidade
	 */
	public void apaga_cidade(String cidade) {
		
		if (!unica_cidade(cidade))
			return;
		
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "DELETE FROM cidades WHERE cidade=? ";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, cidade);
			prep.execute();
			prep.close();
			connec.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	/** Função para apagar uma passagem correspondentes com os parâmetros da função, exibe uma pop up de confirmação.
	 * Se houver apenas apenas uma cidade com passagem, apaga a cidade da lista de 'cidades'.
	 * @param cidade
	 * @param horario
	 */
	public void apaga(String cidade, String horario) {
		try {
			apaga_cidade(cidade);
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "DELETE FROM passagens WHERE destino_cidade=? AND partida_horario=? ";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, cidade);
			prep.setString(2, horario);
			
			int op = JOptionPane.showConfirmDialog(null, "Deseja mesmo apagar a passagem?", "Aviso", 2, JOptionPane.QUESTION_MESSAGE);
			
			if (op == 2)
				return;
			
			prep.execute();
			prep.close();
			connec.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	/** Função para exibir a tabela com os dados correspondente a cidade pesquisada. 
	 * @param cidade 
	 */
	public void pesquisa (String cidade) {
		if (txtDestino.getText().compareTo("") == 0) {
			mostrarTabela();
			return;
		}
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "SELECT destino_cidade, partida_horario, destino_horario, preco FROM passagens WHERE destino_cidade=?";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, cidade);
			ResultSet rs = prep.executeQuery();
			tabelaPassagens.setModel(DbUtils.resultSetToTableModel(rs));
			tabelaPassagens.setDefaultRenderer(Object.class, new CellRenderer(tabelaPassagens));
			
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
	public TabelaPassagens() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1076, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 47, 1040, 401);
		contentPane.add(scrollPane);
		
		tabelaPassagens = new JTable() { /// DESABILITA A EDIÇÃO DAS CÉLULAS
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
		tabelaPassagens.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					int linha_selecionada = tabelaPassagens.getSelectedRow();
					if (linha_selecionada != -1) { /// EXISTE LINHA SELECIONADA
					apaga(tabelaPassagens.getModel().getValueAt(linha_selecionada, 0).toString(),
							tabelaPassagens.getModel().getValueAt(linha_selecionada, 1).toString());
					}
				}
			}
		});
		tabelaPassagens.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabelaPassagens.setBackground(new Color(204, 255, 153));		
		mostrarTabela();
		
		scrollPane.setViewportView(tabelaPassagens);
		
		Image imgadd = new ImageIcon(this.getClass().getResource("/add.png")).getImage();
				
		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NovaPassagem vp = new NovaPassagem();
				vp.setVisible(true);
			}
		});
		btnAdd.setBounds(286, 11, 24, 24);
		btnAdd.setIcon(new ImageIcon(imgadd));
		contentPane.add(btnAdd);
		
		Image imgdel = new ImageIcon(this.getClass().getResource("/trash.png")).getImage();
		
		JButton btnApaga = new JButton("");
		btnApaga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linha_selecionada = tabelaPassagens.getSelectedRow();
				if (linha_selecionada != -1) { // EXISTE LINHA SELECIONADA
				apaga(tabelaPassagens.getModel().getValueAt(linha_selecionada, 0).toString(),
						tabelaPassagens.getModel().getValueAt(linha_selecionada, 1).toString());
				}
			}
		});
		btnApaga.setBounds(318, 11, 24, 24);
		btnApaga.setIcon(new ImageIcon(imgdel));
		contentPane.add(btnApaga);
		
		txtDestino = new JTextField();
		txtDestino.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					pesquisa(txtDestino.getText());
			}
		});
		txtDestino.setBounds(56, 11, 174, 24);
		contentPane.add(txtDestino);
		txtDestino.setColumns(10);
		
		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setBounds(12, 15, 46, 14);
		contentPane.add(lblDestino);
		
		Image imgsearch = new ImageIcon(this.getClass().getResource("/search.png")).getImage();
	
		JButton btnPesquisa = new JButton("");
		btnPesquisa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pesquisa(txtDestino.getText());
			}
		});
		btnPesquisa.setBounds(240, 11, 24, 24);
		btnPesquisa.setIcon(new ImageIcon(imgsearch));
		contentPane.add(btnPesquisa);
	
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(274, 8, 2, 29);
		contentPane.add(separator);
		
		Image imgatualiza = new ImageIcon(this.getClass().getResource("/atualiza.png")).getImage();
		
		JButton btnAtualiza = new JButton("");
		btnAtualiza.setIcon(new ImageIcon(imgatualiza));
		btnAtualiza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarTabela();
			}
		});
		btnAtualiza.setBounds(350, 11, 24, 24);
		contentPane.add(btnAtualiza);
	}
}
