package controller;


import java.awt.Color;
import java.sql.*;


import javax.swing.*;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.border.EmptyBorder;

import model.CellRenderer;
import model.SqliteConnection;
import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import java.awt.Font;

/**
 * @author raquelms203
 *
 */
@SuppressWarnings("serial")
public class TabelaFuncionarios extends JFrame {

	private JPanel contentPane;
	private JTable tabelaFuncionarios;
	private Connection connec = null;
	private JTable tabelaSenha;
	private JScrollPane painelSenha;
	private JButton btnNewButton;
	private boolean flag = false;
	private JButton btnSenhas;
	private JButton btnAdd;
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |  javax.swing.UnsupportedLookAndFeelException ex) {
           System.err.println(ex);        	
        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TabelaFuncionarios frame = new TabelaFuncionarios();
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
	 * Mostra a tabela através da ligação com o banco de dados.
	 */
	public void mostrarTabelaFunc() {
		PreparedStatement prep = null;
		ResultSet rs = null;				
		
		try {
			connec = SqliteConnection.dbBilheteria();
			String query = "SELECT id, nome, user, cargo FROM funcionarios ";
		    prep = connec.prepareStatement(query);
		    rs = prep.executeQuery();
		    tabelaFuncionarios.setModel(DbUtils.resultSetToTableModel(rs));
		    
		    rs.close();
		    prep.close();
		    connec.close();
		
		} catch (SQLException e2) {
			JOptionPane.showMessageDialog(null, e2);
		} 
	}
	
	/**
	 * Mostra a tabela de senhas, com um botão para mostrar/esconder a tabela.
	 */
	public void mostrarTabelaSenha () {
		
		btnSenhas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreparedStatement prep = null;
				ResultSet rs = null;
				
				if (flag) {
					painelSenha.setVisible(false);
					flag = false;
					return;
				}
				try {						
	 				connec = SqliteConnection.dbBilheteria();
					String query = "SELECT senha FROM funcionarios ";
				    prep = connec.prepareStatement(query);
				    rs = prep.executeQuery();
				    tabelaSenha.setModel(DbUtils.resultSetToTableModel(rs));
				    tabelaSenha.setDefaultRenderer(Object.class, new CellRenderer(tabelaSenha));
				    painelSenha.setVisible(true);
				    rs.close();
					prep.close();
					
					} catch (SQLException e2) {
						JOptionPane.showMessageDialog(null, e2);
				    } 
					flag = true;
				}
			});
	}
	



	/**
	 * Create the frame.
	 */
	public TabelaFuncionarios() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1076, 498);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);		

		JScrollPane painelPrincipal = new JScrollPane();
		painelPrincipal.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		painelPrincipal.setBounds(10, 45, 835, 386);
		contentPane.add(painelPrincipal);
		
		tabelaFuncionarios = new JTable();
		
		tabelaFuncionarios.setDefaultRenderer(Object.class, new CellRenderer(tabelaFuncionarios));
		tabelaFuncionarios.setBackground(new Color(173, 216, 230));
		tabelaFuncionarios.setEnabled(false);
		painelPrincipal.setViewportView(tabelaFuncionarios);	
		
		painelSenha = new JScrollPane();
		painelSenha.setBounds(844, 45, 206, 386);
		contentPane.add(painelSenha);
				
		tabelaSenha = new JTable();
		tabelaSenha.setBackground(new Color(250, 128, 114));
		tabelaSenha.setEnabled(false);
		painelSenha.setViewportView(tabelaSenha);
		
		
		btnNewButton = new JButton("New button");
		painelSenha.setColumnHeaderView(btnNewButton);
		
		btnSenhas = new JButton("Senhas");
		btnSenhas.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		btnSenhas.setBounds(886, 11, 124, 26);
		contentPane.add(btnSenhas);
		
		JButton btnPesquisar = new JButton("");		
		Image imgsearch = new ImageIcon(this.getClass().getResource("/search.png")).getImage();
		btnPesquisar.setIcon(new ImageIcon(imgsearch));
		btnPesquisar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AlterarFuncionario af = new AlterarFuncionario();
				af.setVisible(true);
			}
		});
		btnPesquisar.setBounds(10, 11, 24, 24);
		contentPane.add(btnPesquisar);
		
		btnAdd = new JButton("");
		Image imgadd = new ImageIcon(this.getClass().getResource("/add.png")).getImage();
		btnAdd.setIcon(new ImageIcon(imgadd));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NovoFuncionario nf = new NovoFuncionario();
				nf.setVisible(true);
			}
		});
		btnAdd.setBounds(44, 11, 24, 24);
		contentPane.add(btnAdd);
		
		btnAtualiza = new JButton("");
		Image imgatualiza = new ImageIcon(this.getClass().getResource("/atualiza.png")).getImage();
		btnAtualiza.setIcon(new ImageIcon(imgatualiza));
		
		btnAtualiza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mostrarTabelaFunc();
			}
		});
		btnAtualiza.setBounds(78, 11, 24, 24);
		contentPane.add(btnAtualiza);
		
		mostrarTabelaFunc();
		mostrarTabelaSenha();
	}
}
	
	


















