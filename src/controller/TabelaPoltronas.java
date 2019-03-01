package controller;

import java.io.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.SqliteConnection;
import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.SystemColor;
import java.sql.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import javax.swing.border.BevelBorder;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class TabelaPoltronas extends JFrame {

	private JPanel contentPane;
	private JTextField txtA1;
	private JTextField txtA2;
	private JTextField txtB1;
	private JTextField txtB2;
	private JTextField txtC1;
	private JTextField txtC2;
	private JTextField txtD1;
	private JTextField txtE1;
	private JTextField txtD2;
	private JTextField txtE2;
	private JTextField txtA3;
	private JTextField txtA4;
	private JTextField txtB3;
	private JTextField txtB4;
	private JTextField txtC3;
	private JTextField txtC4;
	private JTextField txtD3;
	private JTextField txtD4;
	private JTextField txtE3;
	private JTextField txtE4;
	private JPanel panelPoltronas;
	private JPanel panelLegendas;
	private JButton btnValidar = new JButton("");
	private ArrayList<String> poltronas = new ArrayList<String>();
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
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
			JOptionPane.showMessageDialog(null, ex);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TabelaPoltronas frame = new TabelaPoltronas(3);
					frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
	}

	/**
	 * Função para exibir o valor associado a poltrona individualmente.
	 * 
	 * @param linha
	 * @param coluna
	 * @param id     int referente ao ID da cidade destino, fornecido pelo
	 *               construtor dessa Frame.
	 * @param campo  JTextField referente a linha e coluna
	 */
	public void mostrar_poltronas(char linha, int coluna, int id, JTextField campo) {
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String nome_coluna = ("" + linha + coluna);
			String query = "SELECT " + nome_coluna + " FROM poltronas WHERE id_passagens=" + id + " ";
			PreparedStatement prep = connec.prepareStatement(query);
			ResultSet rs = prep.executeQuery();

			if (rs.getInt(nome_coluna) == 0) {
				campo.setBackground(Color.green);
				campo.setDisabledTextColor(Color.green);
			} else {
				campo.setBackground(Color.red);
				campo.setDisabledTextColor(Color.red);
				campo.setText("1");
			}
			rs.close();
			prep.close();
			connec.close();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e);
		}
		mudar_valor(linha, coluna, id, campo);
	}

	/**
	 * Função que muda o valor da poltrona (mas não valida no banco de dados), usada
	 * na tela em que o usuário ainda está comprando a passagem, um texto com o
	 * valor da poltrona é escrito na mesma cor do fundo, para que não seja visível
	 * ao usuário porém pode ser usado como referência no futuro.
	 * 
	 * @param linha
	 * @param coluna
	 * @param id     int referente ao ID da cidade destino, fornecido pelo
	 *               construtor dessa Frame.
	 * @param campo  JTextField referente a linha e coluna
	 */
	public void mudar_valor(char linha, int coluna, int id, JTextField campo) {
		campo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnValidar.setEnabled(true);
				if (campo.getBackground() == Color.green) {
					campo.setBackground(Color.darkGray);
					campo.setDisabledTextColor(Color.darkGray);
					campo.setText("1");
				} else if (campo.getBackground() == Color.darkGray) {
					campo.setBackground(Color.green);
					campo.setDisabledTextColor(Color.green);
					campo.setText("0");
				} else if (campo.getBackground() == Color.red) {
					return;
				}
			}
		});
	}
	
	/** Adiciona a um ArrayList String, as poltronas selecionadas para a compra. 
	 * @param campo
	 */
	public void poltronas_selecionadas(JTextField campo) {
		if (campo.getBackground() == Color.DARK_GRAY) 
			this.poltronas.add(campo.getName());
	}
	
	/**
	 * Fecha a Frame.
	 */
	public void closeFrame() {
		this.dispose();
	}

	/**
	 * Função para atualizar os valores da poltrona no banco de dados. O valor
	 * default no banco de dados é 0.
	 * 
	 * @param linha
	 * @param coluna
	 * @param id     int referente ao ID da cidade destino, fornecido pelo
	 *               construtor dessa Frame.
	 */
	public void salvar_poltronas(char linha, int coluna, int id, JTextField campo) {
		btnValidar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Connection connec = SqliteConnection.dbBilheteria();
					String query = "UPDATE poltronas SET " + linha + coluna + "='" + campo.getText()
							+ "' WHERE id_passagens='" + id + "'";
					PreparedStatement prep = connec.prepareStatement(query);
					prep.execute();
					prep.close();
					connec.close();
					btnValidar.setEnabled(false);
					poltronas_selecionadas(campo);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
	}

	/**
	 * Função para atribuir as ações da Frame nas poltronas.
	 * 
	 * @param linha
	 * @param coluna
	 * @param id
	 * @param campo
	 */
	public void iniciarPoltrona(char linha, int coluna, int id, JTextField campo) {
		mostrar_poltronas(linha, coluna, id, campo);
		mudar_valor(linha, coluna, id, campo);
		salvar_poltronas(linha, coluna, id, campo);
	}
	
	/**
	 * Getters e Setters.
	 */	
	public ArrayList<String> getPoltronas() {
		return poltronas;
	}

	public void setPoltronas(ArrayList<String> poltronas) {
		this.poltronas = poltronas;
	}

	/**
	 * Create the frame.
	 */
	public TabelaPoltronas(int idPassagem) {
		setAlwaysOnTop(true);
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
			}

			public void windowLostFocus(WindowEvent arg0) {

			}
		});
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		setType(Type.UTILITY);
		setBounds(720, 100, 285, 497);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panelPoltronas = new JPanel();
		panelPoltronas.setBounds(30, 39, 180, 181);
		contentPane.add(panelPoltronas);
		panelPoltronas.setLayout(null);

		txtD1 = new JTextField();
		txtD1.setEnabled(false);
		txtD1.setEditable(false);
		txtD1.setText("0");
		txtD1.setName("D1");
		txtD1.setBounds(0, 131, 30, 20);
		panelPoltronas.add(txtD1);
		txtD1.setColumns(10);
		txtD1.setBackground(Color.GREEN);
		txtD1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('D', 1, idPassagem, txtD1);
			}
		});
		iniciarPoltrona('D', 1, idPassagem, txtD1);

		txtA1 = new JTextField();
		txtA1.setEnabled(false);
		txtA1.setEditable(false);
		txtA1.setText("0");
		txtA1.setName("A1");
		txtA1.setBounds(0, 41, 30, 20);
		panelPoltronas.add(txtA1);
		txtA1.setBackground(Color.GREEN);
		mudar_valor('A', 1, idPassagem, txtA1);
		txtA1.setColumns(10);
		txtA1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('A', 1, idPassagem, txtA1);
			}
		});

		txtA2 = new JTextField();
		txtA2.setEnabled(false);
		txtA2.setEditable(false);
		txtA2.setText("0");
		txtA2.setName("A2");
		txtA2.setBounds(40, 41, 30, 20);
		panelPoltronas.add(txtA2);
		txtA2.setColumns(10);
		txtA2.setBackground(Color.GREEN);
		txtA2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('A', 2, idPassagem, txtA2);
			}
		});

		txtB1 = new JTextField();
		txtB1.setEnabled(false);
		txtB1.setEditable(false);
		txtB1.setText("0");
		txtB1.setName("B1");
		txtB1.setBounds(0, 71, 30, 20);
		panelPoltronas.add(txtB1);
		txtB1.setColumns(10);
		txtB1.setBackground(Color.GREEN);
		txtB1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('B', 1, idPassagem, txtB1);
			}
		});

		txtB2 = new JTextField();
		txtB2.setEnabled(false);
		txtB2.setEditable(false);
		txtB2.setText("0");
		txtB2.setName("B2");
		txtB2.setBounds(40, 71, 30, 20);
		panelPoltronas.add(txtB2);
		txtB2.setColumns(10);
		txtB2.setBackground(Color.GREEN);
		txtB2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('B', 2, idPassagem, txtB2);
			}
		});
		
				txtC1 = new JTextField();
				txtC1.setEnabled(false);
				txtC1.setEditable(false);
				txtC1.setText("0");
				txtC1.setName("C1");
				txtC1.setBounds(0, 101, 30, 20);
				panelPoltronas.add(txtC1);
				txtC1.setColumns(10);
				txtC1.setBackground(Color.GREEN);
				txtC1.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						mudar_valor('C', 1, idPassagem, txtC1);
					}
				});
				iniciarPoltrona('C', 1, idPassagem, txtC1);

		txtC2 = new JTextField();
		txtC2.setEnabled(false);
		txtC2.setEditable(false);
		txtC2.setText("0");
		txtC2.setName("C2");
		txtC2.setBounds(40, 101, 30, 20);
		panelPoltronas.add(txtC2);
		txtC2.setColumns(10);
		txtC2.setBackground(Color.GREEN);
		txtC2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('C', 2, idPassagem, txtC2);
			}
		});

		txtE1 = new JTextField();
		txtE1.setEnabled(false);
		txtE1.setEditable(false);
		txtE1.setText("0");
		txtE1.setName("E1");
		txtE1.setBounds(0, 161, 30, 20);
		panelPoltronas.add(txtE1);
		txtE1.setColumns(10);
		txtE1.setBackground(Color.GREEN);
		txtE1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('E', 1, idPassagem, txtE1);
			}
		});

		txtD2 = new JTextField();
		txtD2.setEnabled(false);
		txtD2.setEditable(false);
		txtD2.setText("0");
		txtD2.setName("D2");
		txtD2.setBounds(40, 131, 30, 20);
		panelPoltronas.add(txtD2);
		txtD2.setColumns(10);
		txtD2.setBackground(Color.GREEN);
		txtD2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('D', 2, idPassagem, txtD2);
			}
		});

		txtE2 = new JTextField();
		txtE2.setEnabled(false);
		txtE2.setEditable(false);
		txtE2.setText("0");
		txtE2.setName("E2");
		txtE2.setBounds(40, 161, 30, 20);
		panelPoltronas.add(txtE2);
		txtE2.setColumns(10);
		txtE2.setBackground(Color.GREEN);
		txtE2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('E', 2, idPassagem, txtE2);
			}
		});

		txtA3 = new JTextField();
		txtA3.setEnabled(false);
		txtA3.setEditable(false);
		txtA3.setText("0");
		txtA3.setName("A3");
		txtA3.setBounds(102, 41, 30, 20);
		panelPoltronas.add(txtA3);
		txtA3.setColumns(10);
		txtA3.setBackground(Color.GREEN);
		txtA3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('A', 3, idPassagem, txtA3);
			}
		});

		txtA4 = new JTextField();
		txtA4.setEnabled(false);
		txtA4.setEditable(false);
		txtA4.setText("0");
		txtA4.setName("A4");
		txtA4.setBounds(142, 41, 30, 20);
		panelPoltronas.add(txtA4);
		txtA4.setColumns(10);
		txtA4.setBackground(Color.GREEN);
		txtA4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('A', 4, idPassagem, txtA4);
			}
		});

		txtB3 = new JTextField();
		txtB3.setEnabled(false);
		txtB3.setEditable(false);
		txtB3.setText("0");
		txtB3.setName("B3");
		txtB3.setBounds(102, 71, 30, 20);
		panelPoltronas.add(txtB3);
		txtB3.setColumns(10);
		txtB3.setBackground(Color.GREEN);
		txtB3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('B', 3, idPassagem, txtB3);
			}
		});

		txtB4 = new JTextField();
		txtB4.setEnabled(false);
		txtB4.setEditable(false);
		txtB4.setText("0");
		txtB4.setName("B4");
		txtB4.setBounds(142, 71, 30, 20);
		panelPoltronas.add(txtB4);
		txtB4.setColumns(10);
		txtB4.setBackground(Color.GREEN);
		txtB4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('B', 4, idPassagem, txtB4);
			}
		});

		txtC3 = new JTextField();
		txtC3.setEnabled(false);
		txtC3.setEditable(false);
		txtC3.setText("0");
		txtC3.setName("C3");
		txtC3.setBounds(102, 101, 30, 20);
		panelPoltronas.add(txtC3);
		txtC3.setColumns(10);
		txtC3.setBackground(Color.GREEN);
		txtC3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('C', 3, idPassagem, txtC3);
			}
		});
		txtC4 = new JTextField();
		txtC4.setEnabled(false);
		txtC4.setEditable(false);
		txtC4.setText("0");
		txtC4.setName("C4");
		txtC4.setBounds(142, 101, 30, 20);
		panelPoltronas.add(txtC4);
		txtC4.setColumns(10);
		txtC4.setBackground(Color.GREEN);
		txtC4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('C', 4, idPassagem, txtC4);
			}
		});

		txtD3 = new JTextField();
		txtD3.setEnabled(false);
		txtD3.setEditable(false);
		txtD3.setText("0");
		txtD3.setName("D3");
		txtD3.setBounds(102, 131, 30, 20);
		panelPoltronas.add(txtD3);
		txtD3.setColumns(10);
		txtD3.setBackground(Color.GREEN);
		txtD3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('D', 3, idPassagem, txtD3);
			}
		});

		txtD4 = new JTextField();
		txtD4.setEnabled(false);
		txtD4.setEditable(false);
		txtD4.setText("0");
		txtD4.setName("D4");
		txtD4.setBounds(142, 131, 30, 20);
		panelPoltronas.add(txtD4);
		txtD4.setColumns(10);
		txtD4.setBackground(Color.GREEN);
		txtD4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('D', 4, idPassagem, txtD4);
			}
		});
		txtE3 = new JTextField();
		txtE3.setEnabled(false);
		txtE3.setEditable(false);
		txtE3.setText("0");
		txtE3.setName("E3");
		txtE3.setBounds(102, 161, 30, 20);
		panelPoltronas.add(txtE3);
		txtE3.setColumns(10);
		txtE3.setBackground(Color.GREEN);
		txtE3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('E', 3, idPassagem, txtE3);
			}
		});
		txtE4 = new JTextField();
		txtE4.setEnabled(false);
		txtE4.setEditable(false);
		txtE4.setText("0");
		txtE4.setName("E4");
		txtE4.setBounds(142, 161, 30, 20);
		panelPoltronas.add(txtE4);
		txtE4.setColumns(10);
		txtE4.setBackground(Color.GREEN);
		txtE4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mudar_valor('E', 4, idPassagem, txtE4);
			}
		});

		panelLegendas = new JPanel();
		panelLegendas.setBounds(10, 79, 192, 168);
		contentPane.add(panelLegendas);
		panelLegendas.setLayout(null);

		JLabel lblA = new JLabel("A");
		lblA.setBounds(0, 0, 20, 20);
		panelLegendas.add(lblA);
		lblA.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblB = new JLabel("B");
		lblB.setBounds(0, 30, 20, 20);
		panelLegendas.add(lblB);
		lblB.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblC = new JLabel("C");
		lblC.setBounds(0, 60, 20, 20);
		panelLegendas.add(lblC);
		lblC.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblD = new JLabel("D");
		lblD.setBounds(0, 92, 20, 20);
		panelLegendas.add(lblD);
		lblD.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblE = new JLabel("E");
		lblE.setBounds(1, 120, 20, 20);
		panelLegendas.add(lblE);
		lblE.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		label = new JLabel("1");
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(30, 142, 20, 20);
		panelLegendas.add(label);
		
		label_1 = new JLabel("2");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_1.setBounds(70, 142, 20, 20);
		panelLegendas.add(label_1);
		
		label_2 = new JLabel("3");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_2.setBounds(129, 142, 20, 20);
		panelLegendas.add(label_2);
		
		label_3 = new JLabel("4");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_3.setBounds(172, 142, 20, 20);
		panelLegendas.add(label_3);

		Image imgok = new ImageIcon(this.getClass().getResource("/ok.png")).getImage();
		Image imgvolante = new ImageIcon(this.getClass().getResource("/volante.png")).getImage();

		btnValidar.setToolTipText("Salvar");
		btnValidar.setIcon(new ImageIcon(imgok));
		btnValidar.setBounds(220, 184, 41, 41);
		contentPane.add(btnValidar);

		iniciarPoltrona('A', 1, idPassagem, txtA1);
		iniciarPoltrona('A', 2, idPassagem, txtA2);
		iniciarPoltrona('A', 3, idPassagem, txtA3);
		iniciarPoltrona('A', 4, idPassagem, txtA4);
		iniciarPoltrona('B', 1, idPassagem, txtB1);
		iniciarPoltrona('B', 2, idPassagem, txtB2);
		iniciarPoltrona('B', 3, idPassagem, txtB3);
		iniciarPoltrona('B', 4, idPassagem, txtB4);
		iniciarPoltrona('C', 2, idPassagem, txtC2);
		iniciarPoltrona('C', 3, idPassagem, txtC3);
		iniciarPoltrona('C', 4, idPassagem, txtC4);
		iniciarPoltrona('D', 2, idPassagem, txtD2);
		iniciarPoltrona('D', 3, idPassagem, txtD3);
		iniciarPoltrona('D', 4, idPassagem, txtD4);
		iniciarPoltrona('E', 1, idPassagem, txtE1);
		iniciarPoltrona('E', 2, idPassagem, txtE2);
		iniciarPoltrona('E', 3, idPassagem, txtE3);
		iniciarPoltrona('E', 4, idPassagem, txtE4);

		JLabel lblVolante = new JLabel("");
		lblVolante.setBounds(0, 0, 30, 33);
		panelPoltronas.add(lblVolante);
		lblVolante.setIcon(new ImageIcon(imgvolante));

	}
}
