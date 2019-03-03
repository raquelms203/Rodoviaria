package view;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import controller.NotaFiscal;
import controller.TabelaPoltronas;
import model.SqliteConnection;
import javax.swing.*;
import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * 
 * @author raquelms203
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class ViewBilheteria extends JFrame {

	private JPanel contentPane;
	private JTextField txtPartida;
	private JTextField txtCliente;
	private JTextField txtPreco;
	private JComboBox comboCDestino = new JComboBox();
	private JComboBox comboHPartida = new JComboBox();
	private JTextField txtHDestino = new JTextField();
	private JTextField txtHorario;
	private int idPassagem = -1;
	private TabelaPoltronas tp = null;
	private JTextField txtTroco;
	private JTextField txtResult;
	private double preco = -1;
	protected JMenuBar menuBar;
	private int id = -1;
	private String caixa = "";
	private String resultado_troco = "";
	private String txt_mes_ano;
	private String txt_data;
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
					ViewBilheteria frame = new ViewBilheteria("");
					frame.setVisible(true);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
	}

	/**
	 * Functions.
	 */

	/**
	 * Função que mostra a tabela 'cidades' ordernado por ordem alfabética.
	 */
	public void mostrarCidades() {
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "  SELECT cidade FROM cidades ORDER BY cidade ASC ";

			PreparedStatement prep;
			prep = connec.prepareStatement(query);
			ResultSet rs = prep.executeQuery();

			while (rs.next()) {
				comboCDestino.addItem(rs.getString("cidade"));
			}
			rs.close();
			prep.close();
			connec.close();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e1);
		}
	}

	/**
	 * Exibe os Horário de Partida de acordo com a cidade passada por parâmetro. Com
	 * essas informações, adiciona ao combo box os Horários de Destino e Preço.
	 * 
	 * @param cidade
	 */
	public void mostrarHPartida(String cidade) {
		try {
			comboHPartida.removeAllItems();

			Connection connec = SqliteConnection.dbBilheteria();
			String query = "SELECT partida_horario, preco FROM passagens WHERE destino_cidade=? "
					+ "		ORDER BY partida_horario DESC";
			String mascara_preco = "";
			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, cidade);
			ResultSet rs;
			rs = prep.executeQuery();

			while (rs.next()) {
				comboHPartida.addItem(rs.getString("partida_horario"));
				this.preco = rs.getDouble("preco");
				mascara_preco = String.format("R$ %.2f", this.preco);
				txtPreco.setText(mascara_preco);
			}
			rs.close();
			prep.close();
			connec.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e);
		}
	}

	/**
	 * Mostra o horário de destino da passagem de acordo com o horário de partida.
	 * 
	 * @param Phorario
	 */
	public int mostrarHDestino() {
		
		try {
			Connection connec = SqliteConnection.dbBilheteria();

			String query = "SELECT destino_horario, id_passagens FROM passagens WHERE destino_cidade=? AND partida_horario=? ";

			PreparedStatement prep = connec.prepareStatement(query);

			prep.setString(1, comboCDestino.getSelectedItem().toString());
			prep.setString(2, comboHPartida.getSelectedItem().toString());
			ResultSet rs = prep.executeQuery();

			while (rs.next()) {
				txtHDestino.setText(rs.getString("destino_horario"));
				id = rs.getInt("id_passagens");
			}

			rs.close();
			prep.close();
			connec.close();
			
			// return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, tp);
		}
		return -1;
	}

	public void calcularTroco() {
		if (!txtTroco.getText().isEmpty()) {
			if (txtTroco.getText().contains(","))
				txtTroco.setText(txtTroco.getText().replace(",", "."));

			double troco = Double.parseDouble(txtTroco.getText());
			double result = troco - this.preco;
			this.resultado_troco = String.format("R$ %.2f", result);
			txtResult.setText(resultado_troco);
			txtResult.setVisible(true);
		}
	}

	public void gerar_txt() {
		
		
		NotaFiscal nf = new NotaFiscal(txtHorario.getText(), this.id, txtPartida.getText(),
		comboCDestino.getSelectedItem().toString(), comboHPartida.getSelectedItem().toString(),
		txtHDestino.getText(), txtCliente.getText().toUpperCase(), tp.getPoltronas().toString(),
		this.caixa, txtPreco.getText(), this.resultado_troco, this.txt_mes_ano, this.txt_data);
	}
	
	/**
	 * Registra a compra. Inserindo na tabela 'compras'.
	 * 
	 * @param caixa
	 */
	public void registraCompra(String caixa) {
		try {
			Connection connec = SqliteConnection.dbBilheteria();
			String query = "INSERT INTO compras (partida_cidade, destino_cidade, partida_horario, destino_horario, "
					+ " cliente, caixa, data, poltrona) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement prep = connec.prepareStatement(query);
			prep.setString(1, txtPartida.getText());
			prep.setString(2, comboCDestino.getSelectedItem().toString());
			prep.setString(3, comboHPartida.getSelectedItem().toString());
			prep.setString(4, txtHDestino.getText());
			prep.setString(5, txtCliente.getText().toUpperCase());
			prep.setString(6, caixa);
			prep.setString(7, txtHorario.getName());
			prep.setString(8, tp.getPoltronas().toString());

			prep.execute();
			prep.close();
			connec.close();
			gerar_txt();
			JOptionPane.showMessageDialog(tp, "Compra realizada com sucesso!");

			limparCampos();
		} catch (SQLException e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(tp, e);
		}
	}

	/**
	 * Função para iniciar a frame de poltronas.
	 * 
	 */
	public void mostrarPanelPoltronas() {
		tp = new TabelaPoltronas(this.id);
		tp.setVisible(true);
	}

	/**
	 * Função para iniciar uma nova compra após o clique do botão "Comprar".
	 */
	public void limparCampos() {
		comboCDestino.setSelectedIndex(1);
		comboHPartida.setSelectedIndex(-1);
		txtHDestino.setText("");
		txtPreco.setText("");
		txtCliente.setText("");
		tp.closeFrame();
	}
	
	/**
	 * Fecha a frame.
	 */
	public void close_frame() {
		this.dispose();
	}

	/**
	 * Formata a data e horário para dois dígitos.
	 * 
	 * @param data
	 * @return valor formatado.
	 */
	public String mascara_data(int data) {
		if (data < 10)
			return ("0" + data);
		return ("" + data);
	}

	/**
	 * Relógio utilizado no sistema.
	 * 
	 * @return um JTextField com data e horário
	 */
	public JTextField clock() {
		JTextField txtData = new JTextField();
		Thread clock = new Thread() {
			public void run() {
				try {
					while (true) {
						Calendar cal = new GregorianCalendar();

						int dia0 = cal.get(Calendar.DAY_OF_MONTH);
						int mes0 = cal.get(Calendar.MONTH) + 1;
						int ano0 = cal.get(Calendar.YEAR);
						String dia = mascara_data(dia0);
						String mes = mascara_data(mes0);
						String ano = mascara_data(ano0);

						int seg0 = cal.get(Calendar.SECOND);
						int min0 = cal.get(Calendar.MINUTE);
						int hora0 = cal.get(Calendar.HOUR_OF_DAY);

						String seg = mascara_data(seg0);
						String min = mascara_data(min0);
						String hora = mascara_data(hora0);

						txtData.setText(dia + "-" + mes + "-" + ano + " " + hora + ":" + min + ":" + seg+ "");
						// USADO PARA O BANCO DE DADOS
						txtData.setName(ano + "-" + mes + "-" + dia + " " + hora + ":" + min + ":" + seg+ ""); 
						txt_mes_ano = (mes+"-"+ano);
						txt_data = (dia+"-"+mes+" "+hora+"h"+min);
						sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
		};
		clock.start();
		return txtData;
	}

	

	/**
	 * Create the frame.
	 */
	public ViewBilheteria(String caixa) {
		this.caixa = caixa;
		setType(Type.POPUP);
		addWindowFocusListener(new WindowFocusListener() { /// ASSIM QUE O FRAME GANHA FOCO
			public void windowGainedFocus(WindowEvent arg0) {
				comboCDestino.requestFocus(); /// FOCO INICIAL DA FRAME
			}

			public void windowLostFocus(WindowEvent arg0) {
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1079, 497);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtPartida = new JTextField();
		txtPartida.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtPartida.setEditable(false);
		txtPartida.setBounds(67, 56, 231, 44);
		contentPane.add(txtPartida);
		txtPartida.setColumns(10);
		txtPartida.setText("JOAO MONLEVADE");

		mostrarCidades();
		comboCDestino.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				mostrarHPartida(comboCDestino.getSelectedItem().toString());
			}
		});
		comboCDestino.setFont(new Font("Tahoma", Font.PLAIN, 13));
		comboCDestino.setEditable(false);
		comboCDestino.setMaximumRowCount(50);
		comboCDestino.setBounds(368, 56, 231, 44);
		contentPane.add(comboCDestino);
		comboHPartida.setBackground(SystemColor.inactiveCaption);
		comboHPartida.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboHPartida.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (tp != null)
					tp.dispose();
				mostrarHDestino();
				mostrarPanelPoltronas();
			}
		});

		comboHPartida.setMaximumRowCount(50);
		comboHPartida.setBounds(67, 148, 92, 44);
		contentPane.add(comboHPartida);
		txtHDestino.setBackground(SystemColor.controlHighlight);
		txtHDestino.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtHDestino.setEditable(false);
		txtHDestino.setBounds(206, 149, 92, 44);
		contentPane.add(txtHDestino);

		txtCliente = new JTextField();
		txtCliente.setText(" ");
		
		txtCliente.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					registraCompra(caixa);
			}
		});
		txtCliente.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtCliente.setBounds(368, 149, 231, 44);
		contentPane.add(txtCliente);
		txtCliente.setColumns(10);

		JLabel lblOrigem = new JLabel("Origem");
		lblOrigem.setBounds(68, 39, 46, 14);
		contentPane.add(lblOrigem);

		JLabel lblDestino = new JLabel("Destino");
		lblDestino.setBounds(368, 39, 46, 14);
		contentPane.add(lblDestino);

		JLabel lblNewLabel = new JLabel("Partida\r\n");
		lblNewLabel.setBounds(67, 133, 62, 14);
		contentPane.add(lblNewLabel);

		JLabel lblChegada = new JLabel("Chegada");
		lblChegada.setBounds(206, 133, 62, 14);
		contentPane.add(lblChegada);

		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setBounds(368, 133, 46, 14);
		contentPane.add(lblCliente);

		txtPreco = new JTextField();
		txtPreco.setEditable(false);
		txtPreco.setHorizontalAlignment(SwingConstants.CENTER);
		txtPreco.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtPreco.setBackground(new Color(240, 230, 140));
		txtPreco.setBounds(67, 231, 92, 44);
		contentPane.add(txtPreco);
		txtPreco.setColumns(10);

		JLabel lblPreco = new JLabel("Pre\u00E7o");
		lblPreco.setBounds(67, 214, 46, 14);
		contentPane.add(lblPreco);

		Image imgok = new ImageIcon(this.getClass().getResource("/ok.png")).getImage();

		JButton btnComprar = new JButton("Comprar");
		btnComprar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtCliente.getText().isEmpty()) {
					JOptionPane.showMessageDialog(tp, "Campo 'Cliente' vazio!", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (tp.getPoltronas().isEmpty()) {
					JOptionPane.showMessageDialog(tp, "Selecione uma poltrona!", "", JOptionPane.ERROR_MESSAGE);
					return;
				}

				registraCompra(caixa);
			}
		});
		btnComprar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnComprar.setBounds(294, 342, 168, 44);
		btnComprar.setIcon(new ImageIcon(imgok));
		contentPane.add(btnComprar);

		txtHorario = clock();
		txtHorario.setBounds(484, 4, 115, 24);
		contentPane.add(txtHorario);
		txtHorario.setColumns(10);

		Image imgatualiza = new ImageIcon(this.getClass().getResource("/atualiza.png")).getImage();

		JButton btnAtualiza = new JButton("");
		btnAtualiza.setToolTipText("Atualizar");
		btnAtualiza.setIcon(new ImageIcon(imgatualiza));
		btnAtualiza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboCDestino.removeAllItems();
				mostrarCidades();
				mostrarHPartida(comboCDestino.getSelectedItem().toString());
				mostrarHDestino();
			}
		});
		btnAtualiza.setBounds(67, 4, 24, 24);
		contentPane.add(btnAtualiza);

		txtTroco = new JTextField();
		txtTroco.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					calcularTroco();
			}
		});
		txtTroco.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtTroco.setBounds(206, 231, 92, 44);
		contentPane.add(txtTroco);
		txtTroco.setColumns(10);

		JLabel lblTroco = new JLabel("Troco?");
		lblTroco.setBounds(206, 214, 46, 14);
		contentPane.add(lblTroco);

		txtResult = new JTextField();
		txtResult.setBackground(new Color(102, 204, 153));
		txtResult.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtResult.setEnabled(true);
		txtResult.setVisible(false);
		txtResult.setColumns(10);
		txtResult.setBounds(308, 231, 92, 44);
		contentPane.add(txtResult);

		Image imgoff = new ImageIcon(this.getClass().getResource("/off.png")).getImage();

		menuBar = new JMenuBar();
		menuBar.setBounds(100, 5, 24, 24);
		contentPane.add(menuBar);

		JMenu menu = new JMenu("");
		menu.setOpaque(true);
		menu.setBackground(new Color(220, 220, 220));
		menu.setIcon(new ImageIcon(imgoff));
		menuBar.add(menu);

		JMenuItem menuTrocar = new JMenuItem("Trocar Usu\u00E1rio");
		menuTrocar.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				close_frame();
				Login l = new Login();
				l.main(null);
			}
		});
		menu.add(menuTrocar);

		Image imgsair = new ImageIcon(this.getClass().getResource("/sair.png")).getImage();

		JMenuItem menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(new ActionListener() {
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		menuSair.setIcon(new ImageIcon(imgsair));
		menu.add(menuSair);
	}
	
	/**
	 * Getters e Setters.
	 */

	public JTextField getTxtHorario() {
		return txtHorario;
	}

	public void setTxtHorario(JTextField txtHorario) {
		this.txtHorario = txtHorario;
	}
}
